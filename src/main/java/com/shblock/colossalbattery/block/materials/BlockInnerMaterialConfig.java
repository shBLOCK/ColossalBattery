package com.shblock.colossalbattery.block.materials;

import com.shblock.colossalbattery.ColossalBattery;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

public class BlockInnerMaterialConfig extends BlockConfig {
    public BlockInnerMaterialConfig(EnumInnerMaterialType type) {
        super(
                ColossalBattery._instance,
                "battery_inner_material_" + type.getName(),
                eConfig -> new BlockInnerMaterial(type),
                getDefaultItemConstructor(ColossalBattery._instance)
        );
    }
}
