package com.shblock.colossalbattery;

import com.shblock.colossalbattery.block.*;
import com.shblock.colossalbattery.block.materials.*;
import com.shblock.colossalbattery.tileentity.TileBatteryCoreConfig;
import com.shblock.colossalbattery.tileentity.TileBatteryInterfaceConfig;
import com.shblock.colossalbattery.tileentity.TileMultiBlockDummyConfig;
import org.cyclops.cyclopscore.config.ConfigHandler;

public class Configs {
    public static void RegisterConfig(ConfigHandler configHandler) {
        //Blocks
        configHandler.addConfigurable(new BlockBatteryCoreConfig());
        configHandler.addConfigurable(new BlockBatteryInterfaceConfig());
        configHandler.addConfigurable(new BlockMultiBlockDummyConfig());
        for (EnumInnerTierBlockType type : EnumInnerTierBlockType.values()) {
            configHandler.addConfigurable(new BlockInnerTierMaterialConfig(type));
        }
        for (EnumBatteryWallType type : EnumBatteryWallType.values()) {
            configHandler.addConfigurable(new BlockBatteryWallConfig(type));
        }
        for (EnumInnerMaterialType type : EnumInnerMaterialType.values()) {
            configHandler.addConfigurable(new BlockInnerMaterialConfig(type));
        }

        //TileEntities
        configHandler.addConfigurable(new TileBatteryCoreConfig());
        configHandler.addConfigurable(new TileBatteryInterfaceConfig());
        configHandler.addConfigurable(new TileMultiBlockDummyConfig());
    }
}
