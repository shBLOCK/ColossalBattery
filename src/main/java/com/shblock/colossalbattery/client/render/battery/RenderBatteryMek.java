package com.shblock.colossalbattery.client.render.battery;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.shblock.colossalbattery.GeneralConfig;
import com.shblock.colossalbattery.material.BatteryMaterials;
import com.shblock.colossalbattery.tileentity.EnumIOMode;
import com.shblock.colossalbattery.tileentity.TileBatteryCore;
import mekanism.client.MekanismClient;
import mekanism.client.model.ModelEnergyCube;
import mekanism.client.render.tileentity.RenderEnergyCube;
import mekanism.common.tier.EnergyCubeTier;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.gen.PerlinNoiseGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class RenderBatteryMek extends RenderBatteryBase {
    public static final RenderBatteryMek _instance = new RenderBatteryMek();

    public static final ModelEnergyCube ENERGY_CUBE_RENDER = new ModelEnergyCube();
    public static final ModelEnergyCube.ModelEnergyCore CORE_RENDER = new ModelEnergyCube.ModelEnergyCore();
    public static final MekRender MEK_RENDER = new MekRender();
    public static final List<PerlinNoiseGenerator> NOISE_LIST = new ArrayList<>();

    static {
        Random random = new Random();
        for (int i = 0; i < GeneralConfig.mek_core_render_count*3; i++) {
            NOISE_LIST.add(new PerlinNoiseGenerator(new SharedSeedRandom(random.nextLong()), ImmutableList.of(0)));
        }
    }

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
            for (int i = 0;i < GeneralConfig.mek_core_render_count;i++) {
                matrixStack.push();
                float energy_percentage = tileEntity.getEnergyPercentage();
                matrixStack.translate(-energy_percentage / 2, -energy_percentage / 2, -energy_percentage / 2);
                matrixStack.translate(0.5, 0.5, 0.5);
                matrixStack.scale(energy_percentage, energy_percentage, energy_percentage);
                matrixStack.push();
                matrixStack.translate(0.5D, 0.5D, 0.5D);
                matrixStack.scale(0.4F, 0.4F, 0.4F);
                rotateByNoise(matrixStack, NOISE_LIST.subList(i * 3, i * 3 + 3), partialTicks);
                CORE_RENDER.render(matrixStack, buffer, combinedLight, combinedOverlay, getCubeTier(tileEntity).getBaseTier().getColor(), 1.0F);
                matrixStack.pop();
                matrixStack.pop();
            }
        }
    }

    private void rotateByNoise(MatrixStack matrixStack, List<PerlinNoiseGenerator> noise_list, float partialTicks) {
        float tick = MekanismClient.ticksPassed + partialTicks;
        matrixStack.rotate(Vector3f.XP.rotation((float) (noise_list.get(0).noiseAt(tick / 100D, 0, false) * 3)));
        matrixStack.rotate(Vector3f.YP.rotation((float) (noise_list.get(1).noiseAt(tick / 100D, 0, false) * 3)));
        matrixStack.rotate(Vector3f.ZP.rotation((float) (noise_list.get(2).noiseAt(tick / 100D, 0, false) * 3)));
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
