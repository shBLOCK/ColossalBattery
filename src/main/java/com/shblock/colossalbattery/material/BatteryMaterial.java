package com.shblock.colossalbattery.material;

import com.shblock.colossalbattery.helper.CubeStructure;
import net.minecraft.block.Block;

import java.util.function.Predicate;

public class BatteryMaterial {
    public final String name;
    public final long capacity_pre_block; //capacity pre block in the structure
    public final Predicate<Block> outline_validator;
    public final Predicate<Block> inner_validator;
    public final Predicate<Block> core_validator;

    public BatteryMaterial(String name, long capacity_pre_block, Predicate<Block> outline_validator, Predicate<Block> inner_validator, Predicate<Block> core_validator) {
        this.name = name;
        this.capacity_pre_block = capacity_pre_block;
        this.outline_validator = outline_validator;
        this.inner_validator = inner_validator;
        this.core_validator = core_validator;
    }

    public long calculateCapacity(CubeStructure structure) {
        return Math.multiplyExact(structure.getBlockCount(), capacity_pre_block);
    }
}
