package com.shblock.colossalbattery.block;

import com.shblock.colossalbattery.tileentity.TileMultiBlockDummy;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class BlockMultiBlockDummy extends BlockMultiBlockPartBase {
    public BlockMultiBlockDummy() {
        super(
                Properties.create(Material.ROCK, MaterialColor.RED)
                        .notSolid()
                        .setOpaque((blockState, world, pos) -> false)
                        .hardnessAndResistance(5.0F)
                        .harvestLevel(0),
                TileMultiBlockDummy::new
        );
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
}
