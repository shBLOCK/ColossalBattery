package com.shblock.colossalbattery.block.materials;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class BlockInnerMaterial extends Block {
    public final EnumInnerMaterialType type;

    public BlockInnerMaterial(EnumInnerMaterialType type) {
        super(
                AbstractBlock.Properties.create(Material.ROCK, MaterialColor.LIME)
                        .hardnessAndResistance(5.0F)
                        .harvestLevel(0)
        );
        this.type = type;
    }

    public static boolean matchMaterialName(BlockState blockState, String name) {
        return matchMaterialName(blockState.getBlock(), name);
    }

    public static boolean matchMaterialName(Block block, String name) {
        if (block instanceof BlockInnerMaterial) {
            return ((BlockInnerMaterial) block).type.getName().equals(name);
        }
        return false;
    }
}
