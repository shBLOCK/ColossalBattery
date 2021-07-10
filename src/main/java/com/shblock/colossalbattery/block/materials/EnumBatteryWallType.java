package com.shblock.colossalbattery.block.materials;

public enum EnumBatteryWallType {
    COPPER("copper"),
    IRON("iron"),
    SILVER("silver"),
    GOLD("gold"),
    DIAMOND("diamond"),
    OBSIDIAN("obsidian"),
    ULTIMATE("ultimate"),
    MEK_BASIC("mek_basic"),
    MEK_ADVANCED("mek_advanced"),
    MEK_ELITE("mek_elite"),
    MEK_ULTIMATE("mek_ultimate");

    private final String material_name;

    EnumBatteryWallType(String material_name) {
        this.material_name = material_name;
    }

    public String getName() {
        return this.material_name;
    }
}
