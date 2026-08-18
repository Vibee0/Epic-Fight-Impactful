package com.nameless.impactful.network;

import com.nameless.impactful.Impactful;
import com.nameless.impactful.client.CameraEngine;
import com.nameless.impactful.config.ClientConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CPApplyShake(int time, float strength, float frequency, int decay_time) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CPApplyShake> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(Impactful.MOD_ID, "shake")
    );

    public static final StreamCodec<ByteBuf, CPApplyShake> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CPApplyShake::time,
            ByteBufCodecs.FLOAT, CPApplyShake::strength,
            ByteBufCodecs.FLOAT, CPApplyShake::frequency,
            ByteBufCodecs.VAR_INT, CPApplyShake::decay_time,
            CPApplyShake::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(final CPApplyShake data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if(!ClientConfig.DISABLE_SCREEN_SHAKE.get())
                CameraEngine.getInstance().shakeCamera(data.strength, data.time, data.frequency, data.decay_time);
        });
    }
}
