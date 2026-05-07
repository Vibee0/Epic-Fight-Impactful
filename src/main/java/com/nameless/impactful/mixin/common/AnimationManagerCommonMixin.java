package com.nameless.impactful.mixin.common;

import com.nameless.impactful.api.PropertiesReader;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;

import java.util.Map;

import static com.nameless.impactful.api.HitStopPropertiesReader.SUBFILE_HS_PROPERTY;
import static com.nameless.impactful.api.PropertiesReader.getSubAnimationFileLocation;
import static yesman.epicfight.api.animation.AnimationManager.getAnimationResourceManager;

@Mixin(AnimationManager.class)
public class AnimationManagerCommonMixin {

    @Shadow @Final
    private Map<AnimationManager.AnimationAccessor<? extends StaticAnimation>, StaticAnimation> animations;

    @Inject(method = "readAnimationProperties(Lyesman/epicfight/api/animation/types/StaticAnimation;)V", at = @At("TAIL"), cancellable = false, remap = false)
    private static void loadHitStop(StaticAnimation animation, CallbackInfo ci){
        ResourceLocation vfxLocation = getSubAnimationFileLocation(animation.getLocation(), SUBFILE_HS_PROPERTY);
        getAnimationResourceManager().getResource(vfxLocation).ifPresent((rs) -> PropertiesReader.readAndApply(animation, rs, SUBFILE_HS_PROPERTY));
    }
}
