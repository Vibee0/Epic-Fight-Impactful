package com.nameless.impactful.capabilities;

import com.nameless.impactful.Impactful;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.bus.api.IEventBus;
import yesman.epicfight.registry.deferred.CustomDataRegister;
import yesman.epicfight.registry.deferred.holders.DeferredCustomData;
import yesman.epicfight.world.capabilities.item.custom.CustomData;

public class ImpactfulCustomData {
    public static final CustomDataRegister REGISTER = CustomDataRegister.createWeapon(Impactful.MOD_ID);

    public static final DeferredCustomData<CustomData<ImpactfulCap.HitStop>> HIT_STOP_CUSTOM_DATA =
            REGISTER.registerCustomData("hit_stop", () -> CustomData.createDeserializable(
                    new ImpactfulCap.HitStop(0, 1f),
                    tag -> {
                        if (tag instanceof CompoundTag compoundTag) {
                            int duration = compoundTag.getInt("duration");
                            float speed = compoundTag.getFloat("speed");
                            return new ImpactfulCap.HitStop(duration, speed);
                        }
                        return new ImpactfulCap.HitStop(0, 1f);
                    }
            )
    );

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
