package com.shblock.colossalbattery;

import com.shblock.colossalbattery.block.BlockBatteryCore;
import com.shblock.colossalbattery.block.BlockBatteryInterface;
import com.shblock.colossalbattery.block.BlockMultiBlockDummy;
import com.shblock.colossalbattery.tileentity.TileBatteryCore;
import com.shblock.colossalbattery.tileentity.TileBatteryInterface;
import com.shblock.colossalbattery.tileentity.TileMultiBlockDummy;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class RegistryEntries {
    @ObjectHolder(ColossalBattery.MODID + ":multi_block_dummy")
    public static final BlockMultiBlockDummy BLOCK_MULTI_BLOCK_DUMMY = null;
    @ObjectHolder(ColossalBattery.MODID + ":multi_block_dummy")
    public static final TileEntityType<TileMultiBlockDummy>TILE_MULTI_BLOCK_DUMMY = null;

    @ObjectHolder(ColossalBattery.MODID + ":battery_core")
    public static final BlockBatteryCore BLOCK_BATTERY_CORE = null;
    @ObjectHolder(ColossalBattery.MODID + ":battery_core")
    public static final TileEntityType<TileBatteryCore> TILE_BATTERY_CORE = null;

    @ObjectHolder(ColossalBattery.MODID + ":battery_interface")
    public static final BlockBatteryInterface BLOCK_BATTERY_INTERFACE = null;
    @ObjectHolder(ColossalBattery.MODID + ":battery_interface")
    public static final TileEntityType<TileBatteryInterface> TILE_BATTERY_INTERFACE = null;
}
