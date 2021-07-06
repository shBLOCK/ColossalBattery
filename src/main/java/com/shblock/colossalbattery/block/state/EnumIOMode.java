package com.shblock.colossalbattery.block.state;

import net.minecraft.util.IStringSerializable;

public enum EnumIOMode implements IStringSerializable {
    NORMAL("normal"),
    INPUT("input"),
    OUTPUT("output");

    private final String name;

    private EnumIOMode(String name) {
        this.name = name;
    }

    @Override
    public String getString() {
        return this.name;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
