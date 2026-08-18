package com.nameless.impactful.network;

import com.nameless.impactful.Impactful;
import com.nameless.impactful.capabilities.ImpactfulAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CPApplyHitStop(int entityId, boolean hit_stop) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CPApplyHitStop> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(Impactful.MOD_ID, "hit_stop")
    );

    public static final StreamCodec<ByteBuf, CPApplyHitStop> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CPApplyHitStop::entityId,
            ByteBufCodecs.BOOL, CPApplyHitStop::hit_stop,
            CPApplyHitStop::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(final CPApplyHitStop data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = context.player().level().getEntity(data.entityId());
            if (entity instanceof Player targetPlayer) {
                targetPlayer.getData(ImpactfulAttachments.HIT_STOP).HIT_STOP = data.hit_stop();
            }
        });
    }
}
