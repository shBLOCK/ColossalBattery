package com.shblock.colossalbattery.block;

import com.shblock.colossalbattery.ColossalBattery;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

public class BlockBatteryCoreConfig extends BlockConfig {
    public BlockBatteryCoreConfig() {
        super(
                ColossalBattery._instance,
                "battery_core",
                eConfig -> new BlockBatteryCore(),
                getDefaultItemConstructor(ColossalBattery._instance)
        );
    }
}
