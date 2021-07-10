package com.shblock.colossalbattery.block.materials;

public enum EnumInnerMaterialType {
    MEK_BASIC("mek_basic", true),
    MEK_ADVANCED("mek_advanced", true),
    MEK_ELITE("mek_elite", true),
    MEK_ULTIMATE("mek_ultimate", true);

    private final String material_name;
    private final boolean is_transparent;

    EnumInnerMaterialType(String material_name, boolean is_transparent) {
        this.material_name = material_name;
        this.is_transparent = is_transparent;
    }

    public String getName() {
        return this.material_name;
    }

    public boolean isTransparent() {
        return this.is_transparent;
    }
}
