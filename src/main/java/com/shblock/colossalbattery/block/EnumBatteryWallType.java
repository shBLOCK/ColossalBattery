package com.shblock.colossalbattery.block;

public enum EnumBatteryWallType {
    COPPER("copper"),
    IRON("iron"),
    SILVER("silver"),
    GOLD("gold"),
    DIAMOND("diamond"),
    OBSIDIAN("obsidian"),
    ULTIMATE("ultimate");

    private final String material_name;

    EnumBatteryWallType(String material_name) {
        this.material_name = material_name;
    }

    public String getName() {
        return this.material_name;
    }
}
