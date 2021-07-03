package com.shblock.colossalbattery.helper;

import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

public class CubeStructure {
    public final BlockPos min_pos;
    public final BlockPos max_pos;

    public CubeStructure(BlockPos min_pos, BlockPos max_pos) {
        this.min_pos = min_pos;
        this.max_pos = max_pos;
    }

    public int[] getSize() {
        return new int[] {
            max_pos.getX() - min_pos.getX(),
            max_pos.getY() - min_pos.getY(),
            max_pos.getZ() - min_pos.getZ()
        };
    }

    public int getBlockCount() {
        return Arrays.stream(getSize()).reduce(1, (a, b) -> a * b);
    }
}
