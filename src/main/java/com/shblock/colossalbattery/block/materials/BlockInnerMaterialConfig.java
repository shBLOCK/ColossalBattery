package com.shblock.colossalbattery.block.materials;

import com.shblock.colossalbattery.ColossalBattery;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

public class BlockInnerMaterialConfig extends BlockConfig {
    private final EnumInnerMaterialType type;

    public BlockInnerMaterialConfig(EnumInnerMaterialType type) {
        super(
                ColossalBattery._instance,
                "battery_inner_material_" + type.getName(),
                eConfig -> new BlockInnerMaterial(type),
                getDefaultItemConstructor(ColossalBattery._instance)
        );
        this.type = type;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(getInstance(), RenderType.getTranslucent());
    }
}
