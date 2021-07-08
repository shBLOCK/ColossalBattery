package com.shblock.colossalbattery.block;

public enum EnumInnerBlockType {
    BASIC("basic", 1),
    ADVANCED("advanced", 2),
    ULTIMATE("ultimate", 10);

    private final String material_name;
    private final int tier;

    EnumInnerBlockType(String material_name, int tier) {
        this.material_name = material_name;
        this.tier = tier;
    }

    public String getName() {
        return this.material_name;
    }

    public int getTier() {
        return this.tier;
    }
}
