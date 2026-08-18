package com.nameless.impactful.capabilities;

import com.nameless.impactful.Impactful;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ImpactfulAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, Impactful.MOD_ID
    );

    public static final Supplier<AttachmentType<HitStopData>> HIT_STOP =
            ATTACHMENT_TYPES.register("hit_stop", () -> AttachmentType.builder(HitStopData::new).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
