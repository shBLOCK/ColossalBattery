package com.shblock.colossalbattery;

import net.minecraftforge.fml.config.ModConfig;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfig;

public class GeneralConfig extends DummyConfig {
    @ConfigurableProperty(
            category = "structure",
            comment = "The maxium size of colossal structure (default = 20).",
            configLocation = ModConfig.Type.SERVER,
            minimalValue = 2,
            maximalValue = 100)
    public static int max_size = 20;

    public GeneralConfig() {
        super(ColossalBattery._instance, "structure");
    }
}
