package com.nameless.impactful.network;

import com.nameless.impactful.Impactful;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Impactful.MOD_ID)
public class NetWorkManger {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToClient(
                CPApplyVFX.TYPE,
                CPApplyVFX.STREAM_CODEC,
                CPApplyVFX::handler
        );

        registrar.playToClient(
                CPApplyShake.TYPE,
                CPApplyShake.STREAM_CODEC,
                CPApplyShake::handler
        );

        registrar.playToClient(
                CPApplyBlur.TYPE,
                CPApplyBlur.STREAM_CODEC,
                CPApplyBlur::handler
        );

        registrar.playToClient(
                CPApplyHitStop.TYPE,
                CPApplyHitStop.STREAM_CODEC,
                CPApplyHitStop::handler
        );
    }

    public static void sendToPlayer(CustomPacketPayload message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

    public static void sendToPlayersTrackingEntityAndSelf(CustomPacketPayload message, ServerPlayer player) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, message);
    }
}
