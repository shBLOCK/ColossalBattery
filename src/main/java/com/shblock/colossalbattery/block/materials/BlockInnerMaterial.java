package com.shblock.colossalbattery.block.materials;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockInnerMaterial extends Block {
    public final EnumInnerMaterialType type;

    public BlockInnerMaterial(EnumInnerMaterialType type) {
        super(
                AbstractBlock.Properties.create(Material.ROCK, MaterialColor.LIME)
                        .hardnessAndResistance(5.0F)
                        .harvestLevel(0)
                        .setOpaque((blockState, world, pos) -> false)
                        .notSolid()
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

    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return this.type.isTransparent() ? 1.0F : super.getAmbientOcclusionLightValue(state, worldIn, pos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return this.type.isTransparent();
    }
}
