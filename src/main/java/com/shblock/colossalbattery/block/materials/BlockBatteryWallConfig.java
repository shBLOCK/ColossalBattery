package com.shblock.colossalbattery.block.materials;

import com.shblock.colossalbattery.ColossalBattery;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

public class BlockBatteryWallConfig extends BlockConfig {
    public BlockBatteryWallConfig(EnumBatteryWallType type) {
        super(
                ColossalBattery._instance,
                "battery_wall_" + type.getName(),
                eConfig -> new BlockBatteryWall(type),
                getDefaultItemConstructor(ColossalBattery._instance)
        );
    }
}
