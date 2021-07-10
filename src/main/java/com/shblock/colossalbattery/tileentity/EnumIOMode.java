package com.shblock.colossalbattery.tileentity;

import net.minecraft.util.IStringSerializable;

public enum EnumIOMode {
    NORMAL(0),
    OUTPUT(1),
    INPUT(2);

    private final int id;

    private EnumIOMode(int id) {
        this.id = id;
    }

    public EnumIOMode getNext() {
        int next_id = getId() + 1;
        if (next_id > 2) {
            next_id = 0;
        }
        return getById(next_id);
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
}
