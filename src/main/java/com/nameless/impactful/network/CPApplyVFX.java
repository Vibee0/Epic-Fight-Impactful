package com.nameless.impactful.network;

import com.nameless.impactful.Impactful;
import com.nameless.impactful.client.CameraEngine;
import com.nameless.impactful.client.RadialBlurEngine;
import com.nameless.impactful.config.ClientConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CPApplyVFX(int weaponCategoryId, int animationId, float elapsedTime) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CPApplyVFX> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(Impactful.MOD_ID, "vfx")
    );

    public static final StreamCodec<ByteBuf, CPApplyVFX> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CPApplyVFX::weaponCategoryId,
            ByteBufCodecs.VAR_INT, CPApplyVFX::animationId,
            ByteBufCodecs.FLOAT, CPApplyVFX::elapsedTime,
            CPApplyVFX::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(final CPApplyVFX data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if(!ClientConfig.DISABLE_SCREEN_SHAKE.get())
                CameraEngine.getInstance().shakeCameraByAnim(data.animationId(), data.elapsedTime());
            if(!ClientConfig.DISABLE_RADIAL_BLUR.get())
                RadialBlurEngine.getInstance().applyRadialBlurByAnim(data.animationId(), data.elapsedTime());
        });
    }
}
