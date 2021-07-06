package com.shblock.colossalbattery.block;

import com.shblock.colossalbattery.ColossalBattery;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

public class BlockInnerMaterialConfig extends BlockConfig {
    public BlockInnerMaterialConfig(EnumInnerBlockType type) {
        super(
                ColossalBattery._instance,
                "battery_inner_" + type.getName(),
                eConfig -> new BlockInnerMaterial(type),
                getDefaultItemConstructor(ColossalBattery._instance)
        );
    }
}
