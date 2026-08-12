package net.montoyo.wd.net;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.montoyo.wd.net.client_bound.*;
import net.montoyo.wd.net.server_bound.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class WDNetworkRegistry {
    public static final int networkingVersion = 2;
    private static final List<NetworkEntry<?>> ENTRIES = new ArrayList<>();
    private static final Map<Class<?>, Integer> IDS = new IdentityHashMap<>();

    public static final LegacyChannel INSTANCE = new LegacyChannel();

    static {
        add(S2CMessageServerInfo.class, S2CMessageServerInfo::new);
        add(C2SMessageMiniservConnect.class, C2SMessageMiniservConnect::new);
        add(S2CMessageMiniservKey.class, S2CMessageMiniservKey::new);
        add(S2CMessageCloseGui.class, S2CMessageCloseGui::new);
        add(S2CMessageOpenGui.class, S2CMessageOpenGui::new);
        add(S2CMessageAddScreen.class, S2CMessageAddScreen::new);
        add(C2SMessageScreenCtrl.class, C2SMessageScreenCtrl::new);
        add(S2CMessageScreenUpdate.class, S2CMessageScreenUpdate::new);
        add(C2SMessageRedstoneCtrl.class, C2SMessageRedstoneCtrl::new);
        add(C2SMessageACQuery.class, C2SMessageACQuery::new);
        add(S2CMessageACResult.class, S2CMessageACResult::new);
        add(S2CMessageJSResponse.class, S2CMessageJSResponse::new);
        add(C2SMessageMinepadUrl.class, C2SMessageMinepadUrl::new);
    }

    private WDNetworkRegistry() {
    }

    private static <T extends Packet> void add(Class<T> type, java.util.function.Function<FriendlyByteBuf, T> decoder) {
        IDS.put(type, ENTRIES.size());
        ENTRIES.add(new NetworkEntry<>(type, decoder));
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar(Integer.toString(networkingVersion))
                .playBidirectional(
                        LegacyPayload.TYPE,
                        LegacyPayload.STREAM_CODEC,
                        WDNetworkRegistry::handle,
                        WDNetworkRegistry::handle
                );
    }

    private static void handle(LegacyPayload payload, IPayloadContext context) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.wrappedBuffer(payload.data()));
        int id = buffer.readVarInt();
        if (id < 0 || id >= ENTRIES.size())
            throw new IllegalArgumentException("Unknown WebDisplays packet id " + id);

        Packet packet = ENTRIES.get(id).decode(buffer);
        packet.handle(new NeoPacketContext(context));
    }

    private static LegacyPayload encode(Packet packet) {
        Integer id = IDS.get(packet.getClass());
        if (id == null) throw new IllegalArgumentException("Unregistered WebDisplays packet " + packet.getClass().getName());

        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeVarInt(id);
        packet.write(buffer);
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.getBytes(buffer.readerIndex(), bytes);
        buffer.release();
        return new LegacyPayload(bytes);
    }

    public static Target player(ServerPlayer player) {
        return new PlayerTarget(player);
    }

    public static Target near(TargetPoint point) {
        return new NearTarget(point);
    }

    public sealed interface Target permits PlayerTarget, NearTarget {
    }

    public record PlayerTarget(ServerPlayer player) implements Target {
    }

    public record NearTarget(TargetPoint point) implements Target {
    }

    public record TargetPoint(
            @Nullable ServerPlayer excluded,
            double x,
            double y,
            double z,
            double radius,
            ResourceKey<Level> dimension
    ) {
    }

    public static final class LegacyChannel {
        public void sendToServer(Packet packet) {
            ClientPacketDistributor.sendToServer(encode(packet));
        }

        public void send(Packet packet, Target target) {
            LegacyPayload payload = encode(packet);
            if (target instanceof PlayerTarget playerTarget) {
                PacketDistributor.sendToPlayer(playerTarget.player(), payload);
            } else if (target instanceof NearTarget nearTarget) {
                TargetPoint point = nearTarget.point();
                ServerLevel level = point.excluded() != null
                        ? (ServerLevel) point.excluded().level()
                        : net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer()
                                .getLevel(point.dimension());
                if (level != null) {
                    PacketDistributor.sendToPlayersNear(
                            level, point.excluded(), point.x(), point.y(), point.z(), point.radius(), payload
                    );
                }
            }
        }

        public void reply(Packet packet, PacketContext context) {
            context.reply(encode(packet));
        }
    }

    private record NeoPacketContext(IPayloadContext delegate) implements PacketContext {
        @Override
        public boolean isClientSide() {
            return delegate.flow() == PacketFlow.CLIENTBOUND;
        }

        @Override
        public @Nullable ServerPlayer getSender() {
            return delegate.player() instanceof ServerPlayer player ? player : null;
        }

        @Override
        public void enqueueWork(Runnable task) {
            delegate.enqueueWork(task);
        }

        @Override
        public void reply(LegacyPayload payload) {
            delegate.reply(payload);
        }
    }
}
