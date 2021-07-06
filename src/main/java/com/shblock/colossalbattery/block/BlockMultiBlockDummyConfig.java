package com.shblock.colossalbattery.block;

import com.shblock.colossalbattery.ColossalBattery;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

public class BlockMultiBlockDummyConfig extends BlockConfig {
    public BlockMultiBlockDummyConfig() {
        super(
                ColossalBattery._instance,
                "multi_block_dummy",
                eConfig -> new BlockMultiBlockDummy(),
                getDefaultItemConstructor(ColossalBattery._instance)
        );
    }
}
