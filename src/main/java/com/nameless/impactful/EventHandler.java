package com.nameless.impactful;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.nameless.impactful.capabilities.ImpactfulCap;
import com.nameless.impactful.command.RadialBlurCommand;
import com.nameless.impactful.command.ShakeCameraCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event){
        event.getDispatcher().register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("impactful")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(ShakeCameraCommand.register())
                                .then(RadialBlurCommand.register())
                        )
        );
    }

    @SubscribeEvent
    public void onPlayerJoinWorld(EntityJoinLevelEvent event){
        if (event.getEntity() instanceof Player player) {
            new ImpactfulCap().onInitiate(player);
        }
    }
    /*
    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingTickEvent event){
        if(event.getEntity() instanceof Player player) {
            if(player.level().isClientSide() || CommonConfig.DISABLE_HIT_STOP.get()) return;
            ImpactfulCap impactfulCap = player.getCapability(ImpactfulCapabilities.INSTANCE).orElse(null);
            if(impactfulCap != null){
                impactfulCap.onUpdate(player);
            }
        }
    }

     */
}
