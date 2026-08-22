package com.nameless.impactful.client;

import com.nameless.impactful.config.ClientConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.client.events.engine.RenderEngine;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;

import java.util.Comparator;
import java.util.PriorityQueue;

import static com.nameless.impactful.api.client.VFXPropertiesReader.VFXAnimationProperties.SCREEN_SHAKE;

@OnlyIn(Dist.CLIENT)
public class CameraEngine {
    public CameraEngine(){
        instance = this;
    }
    private static CameraEngine instance;
    public static CameraEngine getInstance(){
        return instance;
    }
    public PriorityQueue<ShakeEntry> getQueue(){
        return this.queue;
    }
    private final PriorityQueue<ShakeEntry> queue =
            new PriorityQueue<>(Comparator.comparingDouble(e -> -e.strength));
    private final ShakeEntry default_entry = new ShakeEntry(1d,3,0.3d, 0);

    public void applyShake(Camera camera, float partialTick) {
        if (ClientConfig.DISABLE_SCREEN_SHAKE.get() || Minecraft.getInstance().isPaused() || queue.isEmpty()) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        queue.removeIf(entry -> {
            entry.remainingTicks--;
            if(entry.remainingTicks < entry.decay_time){
                entry.strength *= 0.98;
                entry.frequency *= 0.98;
            }
            return entry.remainingTicks <= 0;
        });

        if (!queue.isEmpty()) {
            ShakeEntry top = queue.peek();
            double ticksExistedDelta = player.tickCount + partialTick;
            double k = top.strength / 4F * ClientConfig.SCREEN_SHAKE_AMPLITUDE_RATE.get().floatValue();
            double f = top.frequency;

            float pitch = (float) (camera.getYRot() + k * Math.cos(ticksExistedDelta * f + 2));
            float yaw = (float) (camera.getXRot() + k * Math.cos(ticksExistedDelta * f + 1));
            float roll = (float) (camera.getRoll() + k * Math.cos(ticksExistedDelta * f));

            camera.setRotation(pitch, yaw, roll);
        }
    }

    public void shakeCamera(ShakeEntry entry){
        ShakeEntry entry1 = entry.copy();
        queue.add(entry1);
    }
    public void shakeCamera(float strength, int time, float frequency, int decay_time){
        this.shakeCamera(new ShakeEntry(strength, time, frequency, decay_time));
    }
    public void shakeCamera(int time, float strength, int decay_time){
        this.shakeCamera(new ShakeEntry(strength, time, 0.3, decay_time));
    }
    public void shakeCameraByAnim(int animationId, float elapsedTime){
        Player player = Minecraft.getInstance().player;
        if(player == null) return;
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        RenderItemBase renderitembase = RenderEngine.getInstance().getItemRenderer(stack);
        ShakeEntry shakeEntry = ((IRenderItemBase)renderitembase).getShakeEntry() != null ? ((IRenderItemBase)renderitembase).getShakeEntry() : default_entry;
        if(AnimationManager.byId(animationId).get() instanceof AttackAnimation attackAnimation){
            shakeEntry = attackAnimation.getPhaseByTime(elapsedTime).getProperty(SCREEN_SHAKE).orElse(shakeEntry);
        }


        this.shakeCamera(shakeEntry);
    }

    public static class ShakeEntry {
        double strength;
        int remainingTicks;
        int decay_time;
        double frequency;
        public ShakeEntry(double strength, int tick, double frequency, int decay_time) {
            this.strength = strength;
            this.remainingTicks = tick;
            this.frequency = frequency;
            this.decay_time = decay_time;
        }
        public ShakeEntry(double strength, int tick, int decay_time){
            this(strength, tick, 0.3f, decay_time);
        }

        public ShakeEntry copy(){
            return new ShakeEntry(this.strength, this.remainingTicks, this.frequency, this.decay_time);
        }
    }
}
