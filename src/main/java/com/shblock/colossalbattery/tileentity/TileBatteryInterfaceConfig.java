package com.shblock.colossalbattery.tileentity;

import com.google.common.collect.Sets;
import com.shblock.colossalbattery.ColossalBattery;
import com.shblock.colossalbattery.RegistryEntries;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;

public class TileBatteryInterfaceConfig extends TileEntityConfig<TileBatteryInterface> {
    public TileBatteryInterfaceConfig() {
        super(
                ColossalBattery._instance,
                "battery_interface",
                eConfig -> new TileEntityType<>(TileBatteryInterface::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BATTERY_INTERFACE), null)
        );
    }
}
