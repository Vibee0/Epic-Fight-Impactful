package com.nameless.impactful.mixin.client;

import com.nameless.impactful.client.CameraEngine;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private Camera mainCamera;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V", shift = At.Shift.AFTER))
    private void cameraShake(DeltaTracker deltaTracker, CallbackInfo ci) {
        CameraEngine cameraEngine = CameraEngine.getInstance();
        if (cameraEngine != null && !cameraEngine.getQueue().isEmpty()) {
            float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
            cameraEngine.applyShake(this.mainCamera, partialTick);
        }
    }
}
