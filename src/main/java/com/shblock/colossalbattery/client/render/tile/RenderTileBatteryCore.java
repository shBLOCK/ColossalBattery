package com.shblock.colossalbattery.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.shblock.colossalbattery.client.render.battery.RenderBatteryBase;
import com.shblock.colossalbattery.material.BatteryMaterialRenders;
import com.shblock.colossalbattery.material.BatteryMaterials;
import com.shblock.colossalbattery.tileentity.TileBatteryCore;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderTileBatteryCore extends TileEntityRenderer<TileBatteryCore> {
    public RenderTileBatteryCore(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileBatteryCore tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntity.isFormed()) {
            RenderBatteryBase battery_render = BatteryMaterialRenders.getRender(tileEntity.getMaterial());
            matrixStack.push();
            int[] offset = tileEntity.getRenderOffset();
            matrixStack.translate(offset[0], offset[1], offset[2]);
            int[] size = tileEntity.getStructureSize();
            matrixStack.scale((float) size[0], (float) size[1], (float) size[2]);
            battery_render.render(tileEntity, partialTicks, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
            matrixStack.pop();
        }
    }

    @Override
    public boolean isGlobalRenderer(TileBatteryCore te) {
        return true;
    }
}
