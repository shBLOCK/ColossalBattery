package com.shblock.colossalbattery.client.render.battery;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.shblock.colossalbattery.material.BatteryMaterials;
import com.shblock.colossalbattery.tileentity.EnumIOMode;
import com.shblock.colossalbattery.tileentity.TileBatteryCore;
import mekanism.client.MekanismClient;
import mekanism.client.model.ModelEnergyCube;
import mekanism.client.render.tileentity.RenderEnergyCube;
import mekanism.common.tier.EnergyCubeTier;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Set;

public class RenderBatteryMek extends RenderBatteryBase {
    public static final RenderBatteryMek _instance = new RenderBatteryMek();

    public static final ModelEnergyCube ENERGY_CUBE_RENDER = new ModelEnergyCube();
    public static final ModelEnergyCube.ModelEnergyCore CORE_RENDER = new ModelEnergyCube.ModelEnergyCore();
    public static final MekRender MEK_RENDER = new MekRender();
    public static final int CORE_RENDER_COUNT = 8;

    private EnergyCubeTier getCubeTier(TileBatteryCore tile) {
        if (tile.getMaterial() == BatteryMaterials.MEK_BASIC) {
            return EnergyCubeTier.BASIC;
        }
        if (tile.getMaterial() == BatteryMaterials.MEK_ADVANCED) {
            return EnergyCubeTier.ADVANCED;
        }
        if (tile.getMaterial() == BatteryMaterials.MEK_ELITE) {
            return EnergyCubeTier.ELITE;
        }
        if (tile.getMaterial() == BatteryMaterials.MEK_ULTIMATE) {
            return EnergyCubeTier.ULTIMATE;
        }
        return null;
    }

    @Override
    public void render(TileBatteryCore tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.push();
        matrixStack.translate(0.5, -0.5, 0.5);
        ENERGY_CUBE_RENDER.render(matrixStack, buffer, combinedLight, combinedOverlay, getCubeTier(tileEntity), true, false);
        matrixStack.pop();

        if (tileEntity.getEnergy() != 0) {
            for (int i = 0;i < CORE_RENDER_COUNT;i++) {
                matrixStack.push();
                float energy_percentage = tileEntity.getEnergyPercentage();
                matrixStack.translate(-energy_percentage / 2, -energy_percentage / 2, -energy_percentage / 2);
                matrixStack.translate(0.5, 0.5, 0.5);
                matrixStack.scale(energy_percentage, energy_percentage, energy_percentage);
                matrixStack.push();
                matrixStack.translate(0.5D, 0.5D, 0.5D);
                float ticks = (float) MekanismClient.ticksPassed + partialTicks + i * (36F / CORE_RENDER_COUNT);
                matrixStack.scale(0.4F, 0.4F, 0.4F);
                matrixStack.translate(0.0D, Math.sin(Math.toRadians(3.0F * ticks)) / 7.0D, 0.0D);
                float scaledTicks = 4.0F * ticks;
                matrixStack.rotate(Vector3f.YP.rotationDegrees(scaledTicks));
                matrixStack.rotate(RenderEnergyCube.coreVec.rotationDegrees(36.0F + scaledTicks));
                CORE_RENDER.render(matrixStack, buffer, combinedLight, combinedOverlay, getCubeTier(tileEntity).getBaseTier().getColor(), 1.0F);
                matrixStack.pop();
                matrixStack.pop();
            }
        }
    }

    @Override
    public void renderCore(Set<Direction> sides, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        renderInterface(sides, EnumIOMode.NORMAL, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    @Override
    public void renderInterface(Set<Direction> sides, EnumIOMode mode, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.push();
        matrixStack.translate(0.5D, -0.5D, 0.5D);
        float[] port_color = null;
        switch (mode) {
            case NORMAL:
                port_color = new float[] {0.3F, 0.3F, 0.3F};
                break;
            case INPUT:
                port_color = new float[] {0.0F, 0.658F, 0.952F};
                break;
            case OUTPUT:
                port_color = new float[] {1.0F, 0.5F, 0.15F};
                break;
        }
        MEK_RENDER.renderSidesBatched(matrixStack, buffer, combinedLight, combinedOverlay, sides, port_color);
        matrixStack.pop();
    }
}
