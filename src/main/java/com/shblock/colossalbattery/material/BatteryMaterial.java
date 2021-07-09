package com.shblock.colossalbattery.material;

import net.minecraft.block.Block;

import java.util.function.Predicate;

public class BatteryMaterial {
    public final String name;
    private final long capacity_pre_block; //capacity pre block in the structure
    private final int transfer_rate_pre_block;
    public final Predicate<Block> frame_validator;
    public final Predicate<Block> outline_validator;
    public final Predicate<Block> inner_validator;
    public final Predicate<Block> core_validator;
    public final Predicate<Block> interface_validator;
    private final boolean explosion_resistance;

    public BatteryMaterial(String name, long capacity_pre_block, int transfer_rate_pre_block, Predicate<Block> outline_validator, Predicate<Block> inner_validator, Predicate<Block> core_validator, Predicate<Block> interface_validator, boolean explosion_resistance) {
        this(name, capacity_pre_block, transfer_rate_pre_block, outline_validator, outline_validator, inner_validator, core_validator, interface_validator, explosion_resistance);
    }

    public BatteryMaterial(String name, long capacity_pre_block, int transfer_rate_pre_block, Predicate<Block> frame_validator, Predicate<Block> outline_validator, Predicate<Block> inner_validator, Predicate<Block> core_validator, Predicate<Block> interface_validator, boolean explosion_resistance) {
        this.name = name;
        this.capacity_pre_block = capacity_pre_block;
        this.transfer_rate_pre_block = transfer_rate_pre_block;
        this.frame_validator = frame_validator;
        this.outline_validator = outline_validator;
        this.inner_validator = inner_validator;
        this.core_validator = core_validator;
        this.interface_validator = interface_validator;
        this.explosion_resistance = explosion_resistance;
    }

    public boolean isExplosionResistance() {
        return this.explosion_resistance;
    }

    public long calculateCapacity(int block_count) {
        try {
            return Math.multiplyExact(block_count, this.capacity_pre_block);
        } catch (ArithmeticException ignored) {
            return Long.MAX_VALUE;
        }
    }

    public int calculateTransferRate(int block_count) {
        try {
            return Math.multiplyExact(block_count, this.transfer_rate_pre_block);
        } catch (ArithmeticException ignored) {
            return Integer.MAX_VALUE;
        }
    }
}
