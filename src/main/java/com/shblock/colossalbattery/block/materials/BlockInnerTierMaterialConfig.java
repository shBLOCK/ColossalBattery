package com.shblock.colossalbattery.block.materials;

import com.shblock.colossalbattery.ColossalBattery;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

public class BlockInnerTierMaterialConfig extends BlockConfig {
    public BlockInnerTierMaterialConfig(EnumInnerTierBlockType type) {
        super(
                ColossalBattery._instance,
                "battery_inner_tier_" + type.getName(),
                eConfig -> new BlockInnerTierMaterial(type),
                getDefaultItemConstructor(ColossalBattery._instance)
        );
    }
}
