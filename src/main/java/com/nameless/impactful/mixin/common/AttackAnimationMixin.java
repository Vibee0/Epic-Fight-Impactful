package com.nameless.impactful.mixin.common;

import com.nameless.impactful.capabilities.HitStopData;
import com.nameless.impactful.capabilities.ImpactfulAttachments;
import com.nameless.impactful.capabilities.ImpactfulCap;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(AttackAnimation.class)
public class AttackAnimationMixin {

    @Inject(method = "getPlaySpeed(Lyesman/epicfight/world/capabilities/entitypatch/LivingEntityPatch;Lyesman/epicfight/api/animation/types/DynamicAnimation;)F", at = @At("RETURN"), cancellable = true, remap = false)
    public void getPlaySpeed(LivingEntityPatch<?> entitypatch, DynamicAnimation animation, CallbackInfoReturnable<Float> cir) {
        if(entitypatch instanceof PlayerPatch<?> playerPatch){
            Player player = playerPatch.getOriginal();
            HitStopData hitStopData = player.getData(ImpactfulAttachments.HIT_STOP);
            if(hitStopData.HIT_STOP){
                float k = 1;
                if(hitStopData.HIT_STOP_TIME > 0) {
                    k = hitStopData.HIT_STOP_SPEED;
                    hitStopData.HIT_STOP_TIME--;
                } else {
                    ImpactfulCap.setHitStop(false, player);
                }
                cir.setReturnValue(cir.getReturnValueF() * Math.min(k, 1F));
            }
        }
    }
}
