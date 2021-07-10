package com.shblock.colossalbattery.client.render.battery;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.shblock.colossalbattery.tileentity.EnumIOMode;
import com.shblock.colossalbattery.tileentity.TileBatteryCore;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.Direction;

import java.util.Set;

public abstract class RenderBatteryBase {
    public abstract void render(TileBatteryCore tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay);

    public abstract void renderCore(Set<Direction> sides, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay);

    public abstract void renderInterface(Set<Direction> sides, EnumIOMode mode, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay);
}
