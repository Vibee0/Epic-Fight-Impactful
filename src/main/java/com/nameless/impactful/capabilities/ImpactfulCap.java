package com.nameless.impactful.capabilities;

import com.nameless.impactful.api.HitStopPropertiesReader;
import com.nameless.impactful.network.CPApplyHitStop;
import com.nameless.impactful.network.CPApplyVFX;
import com.nameless.impactful.network.NetWorkManger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.event.EpicFightEventHooks;
import yesman.epicfight.api.event.IdentifierProvider;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

public class ImpactfulCap {

    public void onInitiate(Player player) {
        PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
        if (playerPatch != null) {
            // server
            playerPatch.getEventListener().registerEvent(EpicFightEventHooks.Entity.DELIVER_DAMAGE_POST, event -> {
                if (event.getDamageSource() instanceof EpicFightDamageSource && event.getDamageSource().getAnimation().get() instanceof AttackAnimation animation) {
                    int weaponCategoryId = event.getEntityPatch().getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory().universalOrdinal();
                    int animationId = animation.getId();
                    AnimationPlayer animationPlayer = event.getEntityPatch().getAnimator().getPlayerFor(event.getDamageSource().getAnimation());
                    float elapsedTime = animationPlayer != null ? animationPlayer.getElapsedTime() : 0f;
                    NetWorkManger.sendToPlayer(new CPApplyVFX(weaponCategoryId, animationId, elapsedTime), ((ServerPlayer) player));
                }
                    }, IdentifierProvider.permanent());

            // server
            playerPatch.getEventListener().registerEvent(EpicFightEventHooks.Entity.DELIVER_DAMAGE_INCOME, event -> {
                        if (event.getDamageSource().getAnimation().get() instanceof AttackAnimation) {
                            setHitStop(true, player);
                        }
                    }, IdentifierProvider.permanent());

            //server and client
            playerPatch.getEventListener().registerEvent(EpicFightEventHooks.Animation.BEGIN, event -> {
                        if (event.getAnimation().get() instanceof AttackAnimation animation) {
                            CapabilityItem capabilityItem = playerPatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND);

                            AnimationPlayer animationPlayer = event.getEntityPatch().getAnimator().getPlayerFor(animation.getRealAnimation());
                            float elapsedTime = animationPlayer != null ? animationPlayer.getElapsedTime() : 0f;
                            HitStop hitStop = animation.getPhaseByTime(elapsedTime)
                                    .getProperty(HitStopPropertiesReader.HIT_STOP)
                                    .or(() -> capabilityItem.getCustomData(ImpactfulCustomData.HIT_STOP_CUSTOM_DATA))
                                    .orElse(null);
                            if (hitStop != null) {
                                HitStopData hitStopData = player.getData(ImpactfulAttachments.HIT_STOP);
                                hitStopData.HIT_STOP_TIME = hitStop.duration();
                                hitStopData.HIT_STOP_SPEED = hitStop.speed();
                            }
                        }
                    }, IdentifierProvider.permanent());

            // server and client
            playerPatch.getEventListener().registerEvent(EpicFightEventHooks.Animation.END, event -> {
                if (event.getAnimation().get() instanceof  AttackAnimation) {
                    setHitStop(false, player);
                    }
                }, IdentifierProvider.permanent());
        }
    }

    public static void setHitStop(boolean hitStop, Player player) {
        player.getData(ImpactfulAttachments.HIT_STOP).HIT_STOP = hitStop;
        if (player instanceof ServerPlayer serverPlayer) {
            NetWorkManger.sendToPlayersTrackingEntityAndSelf(new CPApplyHitStop(serverPlayer.getId(), hitStop), serverPlayer);
        }
    }

    public record HitStop(int duration, float speed){
        public HitStop copy(){
            return new HitStop(this.duration, this.speed);
        }
    }
}
