package com.nameless.impactful.config;

import net.neoforged.neoforge.common.ModConfigSpec;


public class CommonConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<Boolean> DISABLE_HIT_STOP;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("disable hit stop");
        DISABLE_HIT_STOP = builder.define("disable_hit_stop", false);
        builder.pop();

        SPEC = builder.build();
    }
}
