package com.shblock.colossalbattery.material;

import com.shblock.colossalbattery.GeneralConfig;
import com.shblock.colossalbattery.block.BlockBatteryCore;
import com.shblock.colossalbattery.block.BlockBatteryInterface;
import com.shblock.colossalbattery.block.materials.BlockBatteryWall;
import com.shblock.colossalbattery.block.materials.BlockInnerMaterial;
import com.shblock.colossalbattery.block.materials.BlockInnerTierMaterial;
import com.shblock.colossalbattery.helper.CubeStructure;
import com.shblock.colossalbattery.helper.MultiBlockHelper;
import mekanism.common.tier.EnergyCubeTier;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.TriPredicate;

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
            return block instanceof BlockInnerTierMaterial && ((BlockInnerTierMaterial) block).type.getTier() >= this.min_tier;
        }
    }
    private static final TriPredicate<CubeStructure, BlockPos, Block> INTERFACE_ONLY_ON_FRAME_CHECKER = (structure, pos, block) -> {
        if (block instanceof BlockBatteryCore || block instanceof BlockBatteryInterface) {
            return MultiBlockHelper.isFrame(structure.min_pos, structure.max_pos, pos);
        }
        return true;
    };
    private static final Predicate<Block> AIR_VALIDATOR = block -> block instanceof AirBlock;

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
    public static final BatteryMaterial MEK_BASIC = new BatteryMaterial("mek_basic", 4000000 / GeneralConfig.mek_divider, 4000 / GeneralConfig.mek_divider,
            block -> BlockBatteryWall.matchMaterialName(block, "mek_basic"),
            AIR_VALIDATOR,
            block -> BlockInnerMaterial.matchMaterialName(block, "mek_basic"),
            CORE_VALIDATOR, INTERFACE_VALIDATOR, INTERFACE_ONLY_ON_FRAME_CHECKER, false);
    public static final BatteryMaterial MEK_ADVANCED = new BatteryMaterial("mek_advanced", 16000000 / GeneralConfig.mek_divider, 16000 / GeneralConfig.mek_divider,
            block -> BlockBatteryWall.matchMaterialName(block, "mek_advanced"),
            AIR_VALIDATOR,
            block -> BlockInnerMaterial.matchMaterialName(block, "mek_advanced"),
            CORE_VALIDATOR, INTERFACE_VALIDATOR, INTERFACE_ONLY_ON_FRAME_CHECKER, false);
    public static final BatteryMaterial MEK_ELITE = new BatteryMaterial("mek_elite", 64000000 / GeneralConfig.mek_divider, 64000 / GeneralConfig.mek_divider,
            block -> BlockBatteryWall.matchMaterialName(block, "mek_elite"),
            AIR_VALIDATOR,
            block -> BlockInnerMaterial.matchMaterialName(block, "mek_elite"),
            CORE_VALIDATOR, INTERFACE_VALIDATOR, INTERFACE_ONLY_ON_FRAME_CHECKER, false);
    public static final BatteryMaterial MEK_ULTIMATE = new BatteryMaterial("mek_ultimate", 256000000 / GeneralConfig.mek_divider, 256000 / GeneralConfig.mek_divider,
            block -> BlockBatteryWall.matchMaterialName(block, "mek_ultimate"),
            AIR_VALIDATOR,
            block -> BlockInnerMaterial.matchMaterialName(block, "mek_ultimate"),
            CORE_VALIDATOR, INTERFACE_VALIDATOR, INTERFACE_ONLY_ON_FRAME_CHECKER, false);

    public static final BatteryMaterial[] VALUES = new BatteryMaterial[] {
            COBBLESTONE,
            COPPER,
            IRON,
            SILVER,
            GOLD,
            DIAMOND,
            OBSIDIAN,
            MENRIL,
            ULTIMATE,
            MEK_BASIC,
            MEK_ADVANCED,
            MEK_ELITE,
            MEK_ULTIMATE
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
