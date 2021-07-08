package com.shblock.colossalbattery.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class BlockBatteryWall extends Block {
    public final EnumBatteryWallType type;

    public BlockBatteryWall(EnumBatteryWallType type) {
        super(
                AbstractBlock.Properties.create(Material.ROCK, MaterialColor.GRAY)
                        .hardnessAndResistance(5.0F)
                        .harvestLevel(0)
        );
        this.type = type;
    }

    public static boolean matchMaterialName(BlockState blockState, String name) {
        return matchMaterialName(blockState.getBlock(), name);
    }

    public static boolean matchMaterialName(Block block, String name) {
        if (block instanceof BlockBatteryWall) {
            return ((BlockBatteryWall) block).type.getName().equals(name);
        }
        return false;
    }
}
