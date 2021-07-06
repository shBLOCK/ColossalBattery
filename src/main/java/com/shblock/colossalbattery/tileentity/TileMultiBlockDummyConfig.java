package com.shblock.colossalbattery.tileentity;

import com.google.common.collect.Sets;
import com.shblock.colossalbattery.ColossalBattery;
import com.shblock.colossalbattery.RegistryEntries;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;

public class TileMultiBlockDummyConfig extends TileEntityConfig<TileMultiBlockDummy> {
    public TileMultiBlockDummyConfig() {
        super(
                ColossalBattery._instance,
                "multi_block_dummy",
                eConfig -> new TileEntityType<>(TileMultiBlockDummy::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_MULTI_BLOCK_DUMMY), null)
        );
    }
}
