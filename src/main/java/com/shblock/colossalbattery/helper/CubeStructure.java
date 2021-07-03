package com.shblock.colossalbattery.helper;

import net.minecraft.util.math.BlockPos;

public class CubeStructure {
    public final BlockPos min_pos;
    public final BlockPos max_pos;

    public CubeStructure(BlockPos min_pos, BlockPos max_pos) {
        this.min_pos = min_pos;
        this.max_pos = max_pos;
    }
}
