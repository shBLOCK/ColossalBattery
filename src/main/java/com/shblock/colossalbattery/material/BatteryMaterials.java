package com.shblock.colossalbattery.material;

import com.shblock.colossalbattery.block.BlockBatteryCore;
import com.shblock.colossalbattery.block.BlockBatteryInterface;
import com.shblock.colossalbattery.block.BlockInnerMaterial;
import com.shblock.colossalbattery.client.render.battery.RenderBatteryBase;
import com.shblock.colossalbattery.client.render.battery.RenderBattery6Face;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.HashMap;
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
            CORE_VALIDATOR, INTERFACE_VALIDATOR);
    public static final BatteryMaterial COPPER = new BatteryMaterial("copper", 80000, 80,//TODO
            block -> block.getTags().contains(new ResourceLocation("forge", "cobblestone")),
            new InnerValidatorBase(1),
            CORE_VALIDATOR, INTERFACE_VALIDATOR);
    public static final BatteryMaterial IRON = new BatteryMaterial("iron", 100000, 100,//TODO
            block -> block.getTags().contains(new ResourceLocation("forge", "cobblestone")),
            new InnerValidatorBase(1),
            CORE_VALIDATOR, INTERFACE_VALIDATOR);
    public static final BatteryMaterial SILVER = new BatteryMaterial("silver", 800000, 800,//TODO
            block -> block.getTags().contains(new ResourceLocation("forge", "cobblestone")),
            new InnerValidatorBase(2),
            CORE_VALIDATOR, INTERFACE_VALIDATOR);
    public static final BatteryMaterial GOLD = new BatteryMaterial("gold", 1000000, 1000,//TODO
            block -> block.getTags().contains(new ResourceLocation("forge", "cobblestone")),
            new InnerValidatorBase(2),
            CORE_VALIDATOR, INTERFACE_VALIDATOR);
    public static final BatteryMaterial DIAMOND = new BatteryMaterial("diamond", 10000000, 10000,//TODO
            block -> block.getTags().contains(new ResourceLocation("forge", "cobblestone")),
            new InnerValidatorBase(3),
            CORE_VALIDATOR, INTERFACE_VALIDATOR);
    public static final BatteryMaterial OBSIDIAN = new BatteryMaterial("obsidian", 10000000, 10000,//TODO
            block -> block.getTags().contains(new ResourceLocation("forge", "cobblestone")),
            new InnerValidatorBase(3),
            CORE_VALIDATOR, INTERFACE_VALIDATOR);
    public static final BatteryMaterial MENRIL = new BatteryMaterial("menril", 1000000, 1000,
            block -> block.getRegistryName().equals(new ResourceLocation("integrateddynamics", "crystalized_menril_block")),
            new InnerValidatorBase(2),
            CORE_VALIDATOR, INTERFACE_VALIDATOR);
    public static final BatteryMaterial ULTIMATE = new BatteryMaterial("ultimate", Long.MAX_VALUE / 100, Integer.MAX_VALUE / 100,//TODO
            block -> block.getTags().contains(new ResourceLocation("forge", "cobblestone")),
            new InnerValidatorBase(10),
            CORE_VALIDATOR, INTERFACE_VALIDATOR);

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

    @OnlyIn(Dist.CLIENT)
    public static final HashMap<BatteryMaterial, RenderBatteryBase> RENDERS = new HashMap<>();
    @OnlyIn(Dist.CLIENT)
    private static void initRenderMap() {
        RENDERS.put(COBBLESTONE, RenderBattery6Face._instance);
        RENDERS.put(COPPER, RenderBattery6Face._instance);
        RENDERS.put(IRON, RenderBattery6Face._instance);
        RENDERS.put(SILVER, RenderBattery6Face._instance);
        RENDERS.put(GOLD, RenderBattery6Face._instance);
        RENDERS.put(DIAMOND, RenderBattery6Face._instance);
        RENDERS.put(OBSIDIAN, RenderBattery6Face._instance);
        RENDERS.put(MENRIL, RenderBattery6Face._instance);
        RENDERS.put(ULTIMATE, RenderBattery6Face._instance);
    }

    static {
        if (MinecraftHelpers.isClientSide()) {
            initRenderMap();
        }
    }

    public static BatteryMaterial fromName(String name) {
        for (BatteryMaterial material : VALUES) {
            if (material.name.equals(name)) {
                return material;
            }
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public static RenderBatteryBase getRender(BatteryMaterial material) {
        return RENDERS.get(material);
    }
}
