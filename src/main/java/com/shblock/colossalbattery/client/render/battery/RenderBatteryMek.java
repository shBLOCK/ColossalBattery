package com.shblock.colossalbattery.client.render.battery;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.shblock.colossalbattery.material.BatteryMaterials;
import com.shblock.colossalbattery.tileentity.EnumIOMode;
import com.shblock.colossalbattery.tileentity.TileBatteryCore;
import mekanism.api.RelativeSide;
import mekanism.client.MekanismClient;
import mekanism.client.model.ModelEnergyCube;
import mekanism.client.render.tileentity.RenderEnergyCube;
import mekanism.common.tier.EnergyCubeTier;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

import java.util.HashSet;
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
        Set<RelativeSide> relativeSides = new HashSet<>();
        for (Direction facing : sides) {
            switch (facing) {
                case UP:
                    relativeSides.add(RelativeSide.BOTTOM);
                    break;
                case DOWN:
                    relativeSides.add(RelativeSide.TOP);
                    break;
                case NORTH:
                    relativeSides.add(RelativeSide.FRONT);
                    break;
                case SOUTH:
                    relativeSides.add(RelativeSide.BACK);
                    break;
                case EAST:
                    relativeSides.add(RelativeSide.RIGHT);
                    break;
                case WEST:
                    relativeSides.add(RelativeSide.LEFT);
                    break;
            }
        }
        int light = combinedLight;
        switch (mode) {
            case INPUT:
                light = ColorHelper.PackedColor.packColor(255, 50, 50, 255);
                break;
            case OUTPUT:
                light = ColorHelper.PackedColor.packColor(255, 255, 255, 0);
                break;
        }
        MEK_RENDER.renderSidesBatched(matrixStack, buffer, light, combinedOverlay, relativeSides, false);
        matrixStack.pop();
    }
}
