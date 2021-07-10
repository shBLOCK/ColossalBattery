package com.shblock.colossalbattery.block.materials;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class BlockInnerTierMaterial extends Block {
    public final EnumInnerTierBlockType type;

    public BlockInnerTierMaterial(EnumInnerTierBlockType type) {
        super(
                AbstractBlock.Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY)
                        .hardnessAndResistance(5.0F)
                        .harvestLevel(0)
        );
        this.type = type;
    }
}
