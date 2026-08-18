package com.nameless.impactful;

import com.nameless.impactful.capabilities.ImpactfulAttachments;
import com.nameless.impactful.capabilities.ImpactfulCustomData;
import com.nameless.impactful.client.CameraEngine;
import com.nameless.impactful.client.RadialBlurEngine;
import com.nameless.impactful.config.ClientConfig;
import com.nameless.impactful.config.CommonConfig;
import com.nameless.impactful.network.NetWorkManger;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(Impactful.MOD_ID)
public class Impactful {
    public static final String MOD_ID = "impactful";
    public Impactful(IEventBus bus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        bus.addListener(this::clientSetup);
        bus.register(NetWorkManger.class);
        ImpactfulAttachments.register(bus);
        ImpactfulCustomData.register(bus);
        NeoForge.EVENT_BUS.register(new EventHandler());
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        new CameraEngine();
        new RadialBlurEngine();
    }
}
