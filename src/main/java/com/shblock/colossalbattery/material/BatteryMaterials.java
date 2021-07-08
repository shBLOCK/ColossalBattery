package com.shblock.colossalbattery.material;

import com.shblock.colossalbattery.block.BlockBatteryCore;
import com.shblock.colossalbattery.block.BlockBatteryInterface;
import com.shblock.colossalbattery.block.BlockBatteryWall;
import com.shblock.colossalbattery.block.BlockInnerMaterial;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import java.util.function.Predicate;

public class BatteryMaterials {
    private static final Predicate<Block> CORE_VALIDATOR = block -> block instanceof BlockBatteryCore;
    private static final Predicate<Block> INTERFACE_VALIDATOR = block -> block instanceof BlockBatteryInterface;
    private static class InnerValidatorBase implements Predicate<Block> {
        private final int min_tier;

        public InnerValidatorBase(int min_tier) {
            this.min_tier = min_tier;
        }

        @Override
        public boolean test(Block block) {
            return block instanceof BlockInnerMaterial && ((BlockInnerMaterial) block).type.getTier() >= this.min_tier;
        }
    }

    public static final BatteryMaterial COBBLESTONE = new BatteryMaterial("cobblestone", 10000, 10,
            block -> block.getTags().contains(new ResourceLocation("forge", "cobblestone")),
            block -> block instanceof AirBlock,
            CORE_VALIDATOR, INTERFACE_VALIDATOR, false);
    public static final BatteryMaterial COPPER = new BatteryMaterial("copper", 80000, 80,
            block -> BlockBatteryWall.matchMaterialName(block, "copper"),
            new InnerValidatorBase(1),
            CORE_VALIDATOR, INTERFACE_VALIDATOR, false);
    public static final BatteryMaterial IRON = new BatteryMaterial("iron", 100000, 100,
            block -> BlockBatteryWall.matchMaterialName(block, "iron"),
            new InnerValidatorBase(1),
            CORE_VALIDATOR, INTERFACE_VALIDATOR, false);
    public static final BatteryMaterial SILVER = new BatteryMaterial("silver", 800000, 800,
            block -> BlockBatteryWall.matchMaterialName(block, "silver"),
            new InnerValidatorBase(2),
            CORE_VALIDATOR, INTERFACE_VALIDATOR, false);
    public static final BatteryMaterial GOLD = new BatteryMaterial("gold", 1000000, 1000,
            block -> BlockBatteryWall.matchMaterialName(block, "gold"),
            new InnerValidatorBase(2),
            CORE_VALIDATOR, INTERFACE_VALIDATOR, false);
    public static final BatteryMaterial DIAMOND = new BatteryMaterial("diamond", 10000000, 10000,
            block -> BlockBatteryWall.matchMaterialName(block, "diamond"),
            block -> block.getTags().contains(new ResourceLocation("forge", "storage_blocks/redstone")),
            CORE_VALIDATOR, INTERFACE_VALIDATOR, false);
    public static final BatteryMaterial OBSIDIAN = new BatteryMaterial("obsidian", 10000000, 10000,
            block -> BlockBatteryWall.matchMaterialName(block, "obsidian"),
            block -> block.getTags().contains(new ResourceLocation("forge", "storage_blocks/redstone")),
            CORE_VALIDATOR, INTERFACE_VALIDATOR, true);
    public static final BatteryMaterial MENRIL = new BatteryMaterial("menril", 1000000, 1000,
            block -> block.getRegistryName().equals(new ResourceLocation("integrateddynamics", "crystalized_menril_block")),
            block -> block.getTags().contains(new ResourceLocation("forge", "storage_blocks/redstone")),
            CORE_VALIDATOR, INTERFACE_VALIDATOR, false);
    public static final BatteryMaterial ULTIMATE = new BatteryMaterial("ultimate", Long.MAX_VALUE / 120, Integer.MAX_VALUE / 120,
            block -> BlockBatteryWall.matchMaterialName(block, "ultimate"),
            new InnerValidatorBase(10),
            CORE_VALIDATOR, INTERFACE_VALIDATOR, true);

    public static final BatteryMaterial[] VALUES = new BatteryMaterial[] {
            COBBLESTONE,
            COPPER,
            IRON,
            SILVER,
            GOLD,
            DIAMOND,
            OBSIDIAN,
            MENRIL,
            ULTIMATE
    };

    public static BatteryMaterial fromName(String name) {
        for (BatteryMaterial material : VALUES) {
            if (material.name.equals(name)) {
                return material;
            }
        }
        return null;
    }
}
