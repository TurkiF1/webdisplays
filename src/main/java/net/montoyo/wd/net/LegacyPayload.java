package net.montoyo.wd.net;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record LegacyPayload(byte[] data) implements CustomPacketPayload {
    public static final Type<LegacyPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath("webdisplays", "legacy_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LegacyPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buffer, payload) -> buffer.writeByteArray(payload.data),
                    buffer -> new LegacyPayload(buffer.readByteArray())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
