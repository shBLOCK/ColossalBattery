package com.shblock.colossalbattery.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.shblock.colossalbattery.tileentity.TileBatteryCore;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class RenderTileBatteryCore extends TileEntityRenderer<TileBatteryCore> {
    public RenderTileBatteryCore(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileBatteryCore tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

    }
}
