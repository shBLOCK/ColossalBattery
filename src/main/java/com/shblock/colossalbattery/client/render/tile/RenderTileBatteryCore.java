package com.shblock.colossalbattery.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.shblock.colossalbattery.client.render.battery.RenderBatteryBase;
import com.shblock.colossalbattery.material.BatteryMaterialRenders;
import com.shblock.colossalbattery.material.BatteryMaterials;
import com.shblock.colossalbattery.tileentity.EnumIOMode;
import com.shblock.colossalbattery.tileentity.TileBatteryCore;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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
            if (battery_render == null) return;
            matrixStack.push();
            int[] render_offset = tileEntity.getRenderOffset();
            matrixStack.translate(render_offset[0], render_offset[1], render_offset[2]);
            int[] size = tileEntity.getStructureSize();
            matrixStack.scale((float) size[0], (float) size[1], (float) size[2]);
            battery_render.render(tileEntity, partialTicks, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
            matrixStack.pop();

            battery_render.renderCore(tileEntity.getInterfaceRenderFaces(tileEntity.getPos()), partialTicks, matrixStack, buffer, combinedLightIn, combinedOverlayIn);

            for (BlockPos pos : tileEntity.getInterfaceList()) {
                matrixStack.push();
                BlockPos offset = tileEntity.getInterfaceOffset(pos);
                matrixStack.translate(offset.getX(), offset.getY(), offset.getZ());
                EnumIOMode mode = tileEntity.getInterfaceMode(pos);
                if (mode == null) mode = EnumIOMode.NORMAL;
                battery_render.renderInterface(tileEntity.getInterfaceRenderFaces(pos), mode, partialTicks, matrixStack, buffer, combinedLightIn, combinedOverlayIn);
                matrixStack.pop();
            }
        }
    }

    @Override
    public boolean isGlobalRenderer(TileBatteryCore te) {
        return true;
    }
}
