package com.shblock.colossalbattery.material;

import com.shblock.colossalbattery.client.render.battery.RenderBattery6Face;
import com.shblock.colossalbattery.client.render.battery.RenderBatteryBase;
import com.shblock.colossalbattery.client.render.battery.RenderBatteryMek;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
public class BatteryMaterialRenders {
    @OnlyIn(Dist.CLIENT)
    public static final HashMap<BatteryMaterial, RenderBatteryBase> RENDERS = new HashMap<>();
    @OnlyIn(Dist.CLIENT)
    private static void initRenderMap() {
        RENDERS.put(BatteryMaterials.COBBLESTONE, RenderBattery6Face._instance);
        RENDERS.put(BatteryMaterials.COPPER, RenderBattery6Face._instance);
        RENDERS.put(BatteryMaterials.IRON, RenderBattery6Face._instance);
        RENDERS.put(BatteryMaterials.SILVER, RenderBattery6Face._instance);
        RENDERS.put(BatteryMaterials.GOLD, RenderBattery6Face._instance);
        RENDERS.put(BatteryMaterials.DIAMOND, RenderBattery6Face._instance);
        RENDERS.put(BatteryMaterials.OBSIDIAN, RenderBattery6Face._instance);
        RENDERS.put(BatteryMaterials.MENRIL, RenderBattery6Face._instance);
        RENDERS.put(BatteryMaterials.ULTIMATE, RenderBattery6Face._instance);
        if (ModList.get().isLoaded("mekanism")) {
            RENDERS.put(BatteryMaterials.MEK_BASIC, RenderBatteryMek._instance);
            RENDERS.put(BatteryMaterials.MEK_ADVANCED, RenderBatteryMek._instance);
            RENDERS.put(BatteryMaterials.MEK_ELITE, RenderBatteryMek._instance);
            RENDERS.put(BatteryMaterials.MEK_ULTIMATE, RenderBatteryMek._instance);
        }
    }

    static {
        if (MinecraftHelpers.isClientSide()) {
            initRenderMap();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static RenderBatteryBase getRender(BatteryMaterial material) {
        return RENDERS.get(material);
    }
}
