package com.shblock.colossalbattery.block;

import com.shblock.colossalbattery.ColossalBattery;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

public class BlockBatteryInterfaceConfig extends BlockConfig {
    public BlockBatteryInterfaceConfig() {
        super(
                ColossalBattery._instance,
                "battery_interface",
                eConfig -> new BlockBatteryInterface(),
                getDefaultItemConstructor(ColossalBattery._instance)
        );
    }
}
