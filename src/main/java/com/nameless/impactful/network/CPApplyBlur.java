package com.nameless.impactful.network;

import com.nameless.impactful.Impactful;
import com.nameless.impactful.client.RadialBlurEngine;
import com.nameless.impactful.config.ClientConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CPApplyBlur(int time, float strength, int decay_time) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CPApplyBlur> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(Impactful.MOD_ID, "blur")
    );

    public static final StreamCodec<ByteBuf, CPApplyBlur> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CPApplyBlur::time,
            ByteBufCodecs.FLOAT, CPApplyBlur::strength,
            ByteBufCodecs.VAR_INT, CPApplyBlur::decay_time,
            CPApplyBlur::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(final CPApplyBlur data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if(!ClientConfig.DISABLE_RADIAL_BLUR.get())
                RadialBlurEngine.getInstance().applyRadialBlur(data.time, data.strength, data.decay_time);
        });
    }
}
