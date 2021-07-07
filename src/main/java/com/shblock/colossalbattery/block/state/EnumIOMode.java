package com.shblock.colossalbattery.block.state;

import net.minecraft.util.IStringSerializable;

public enum EnumIOMode implements IStringSerializable {
    NORMAL("normal", 0),
    OUTPUT("output", 1),
    INPUT("input", 2);

    private final String name;
    private final int id;

    private EnumIOMode(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getString() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public static EnumIOMode getById(int id) {
        for (EnumIOMode mode : EnumIOMode.values()) {
            if (mode.id == id) {
                return mode;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
