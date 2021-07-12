package com.shblock.colossalbattery;

import net.minecraftforge.fml.config.ModConfig;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfig;

public class GeneralConfig extends DummyConfig {
    @ConfigurableProperty(
            category = "structure",
            comment = "The maximum size of colossal structure (default = 20).",
            configLocation = ModConfig.Type.SERVER,
            minimalValue = 2,
            maximalValue = 100)
    public static int max_size = 20;

    @ConfigurableProperty(
            category = "structure",
            comment = "The divider of mek battery type's per block storage and transfer rate from mek's energy cube's. (default = 8)",
            configLocation = ModConfig.Type.SERVER,
            minimalValue = 1,
            maximalValue = 10000
    )
    public static int mek_divider = 8;

    @ConfigurableProperty(
            category = "render",
            comment = "How many \"Energy Core\" to render in a colossal mek energy cube. (default = 1)",
            configLocation = ModConfig.Type.COMMON,
            minimalValue = 1,
            maximalValue = 1024,
            requiresWorldRestart = true,
            requiresMcRestart = true
    )
    public static int mek_core_render_count = 1;

    public GeneralConfig() {
        super(ColossalBattery._instance, "general");
    }
}
