package com.shblock.colossalbattery.tileentity;

import com.google.common.collect.Sets;
import com.shblock.colossalbattery.ColossalBattery;
import com.shblock.colossalbattery.RegistryEntries;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;

public class TileBatteryCoreConfig extends TileEntityConfig<TileBatteryCore> {
    public TileBatteryCoreConfig() {
        super(
                ColossalBattery._instance,
                "battery_core",
                eConfig -> new TileEntityType<>(TileBatteryCore::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BATTERY_CORE), null)
        );
    }
}
