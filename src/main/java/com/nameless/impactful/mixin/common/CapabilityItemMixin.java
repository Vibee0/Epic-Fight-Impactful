package com.nameless.impactful.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.Optional;

@Mixin(CapabilityItem.class)
public class CapabilityItemMixin {

    @Redirect(method = "getCustomData", at = @At(value = "INVOKE", target = "Ljava/util/Optional;of(Ljava/lang/Object;)Ljava/util/Optional;"), remap = false)
    private <T> Optional<T> getCustomData(T value) {
        return Optional.ofNullable(value);
    }
}
