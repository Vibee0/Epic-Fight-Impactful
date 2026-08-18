package com.nameless.impactful.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    public static final ModConfigSpec SPEC;
    public static ModConfigSpec.ConfigValue<Double> SCREEN_SHAKE_AMPLITUDE_RATE;
    public static ModConfigSpec.ConfigValue<Boolean> DISABLE_SCREEN_SHAKE;
    public static ModConfigSpec.ConfigValue<Boolean> DISABLE_RADIAL_BLUR;
    public static ModConfigSpec.ConfigValue<Double> RADIAL_BLUR_INTENSITY_RATE;
    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("global setting");
        DISABLE_SCREEN_SHAKE = builder.define("disable_screen_shake", false);
        DISABLE_RADIAL_BLUR = builder.define("disable_radial_blur", false);
        SCREEN_SHAKE_AMPLITUDE_RATE = builder.defineInRange("screen_shake_amplitude",1D,0D,10D);
        RADIAL_BLUR_INTENSITY_RATE = builder.defineInRange("radial_blur_rate",0.025D,0D,10D);
        builder.pop();

        SPEC = builder.build();
    }
}
