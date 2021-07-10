package com.shblock.colossalbattery.block.materials;

public enum EnumInnerMaterialType {
    MEK_BASIC("mek_basic"),
    MEK_ADVANCED("mek_advanced"),
    MEK_ELITE("mek_elite"),
    MEK_ULTIMATE("mek_ultimate");

    private final String material_name;

    EnumInnerMaterialType(String material_name) {
        this.material_name = material_name;
    }

    public String getName() {
        return this.material_name;
    }
}
