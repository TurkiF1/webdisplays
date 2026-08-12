package net.montoyo.wd.net;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

/**
 * Loader-neutral context used by the legacy WebDisplays packets.
 * NeoForge's payload context is adapted to this interface at the network edge.
 */
public interface PacketContext {
    boolean isClientSide();

    default boolean isServerSide() {
        return !isClientSide();
    }

    @Nullable
    ServerPlayer getSender();

    void enqueueWork(Runnable task);

    default void setPacketHandled(boolean handled) {
    }
}
