package com.shblock.colossalbattery.block.materials;

import com.shblock.colossalbattery.ColossalBattery;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

public class BlockInnerTierMaterialConfig extends BlockConfig {
    public BlockInnerTierMaterialConfig(EnumInnerTierBlockType type) {
        super(
                ColossalBattery._instance,
                "battery_inner_" + type.getName(),
                eConfig -> new BlockInnerTierMaterial(type),
                getDefaultItemConstructor(ColossalBattery._instance)
        );
    }
}
