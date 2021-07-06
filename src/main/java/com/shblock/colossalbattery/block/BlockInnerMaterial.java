package com.shblock.colossalbattery.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class BlockInnerMaterial extends Block {
    public final EnumInnerBlockType type;

    public BlockInnerMaterial(EnumInnerBlockType type) {
        super(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.MAGENTA));
        this.type = type;
    }
}
