package com.shblock.colossalbattery.client.render.battery;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.shblock.colossalbattery.ColossalBattery;
import mekanism.api.RelativeSide;
import mekanism.client.model.MekanismJavaModel;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Set;

//All copied from Mek's ModelEnergyCube class!
@OnlyIn(Dist.CLIENT)
public class MekRender extends MekanismJavaModel {
    private static final ResourceLocation CUBE_TEXTURE;
    private final RenderType RENDER_TYPE;
    private final ModelRenderer[] ports;

    static {
        CUBE_TEXTURE = new ResourceLocation(ColossalBattery.MODID, "textures/models/mek/energy_cube.png");
    }

    public static void onPreTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
            event.addSprite(CUBE_TEXTURE);
        }
    }

    public MekRender() {
        super(RenderType::getEntitySolid);
        this.RENDER_TYPE = this.getRenderType(CUBE_TEXTURE);
        this.textureWidth = 64;
        this.textureHeight = 64;
        ModelRenderer portBackToggle = new ModelRenderer(this, 18, 35);
        portBackToggle.addBox(0.0F, 0.0F, 1F, 8.0F, 8.0F, 1.0F, false);
        portBackToggle.setRotationPoint(-4.0F, 12.0F, 7.0F);
        portBackToggle.setTextureSize(64, 64);
        portBackToggle.mirror = true;
        this.setRotation(portBackToggle, 0.0F, 0.0F, 0.0F);
        ModelRenderer portBottomToggle = new ModelRenderer(this, 0, 26);
        portBottomToggle.addBox(0.0F, 1F, 0.0F, 8.0F, 1.0F, 8.0F, false);
        portBottomToggle.setRotationPoint(-4.0F, 23.0F, -4.0F);
        portBottomToggle.setTextureSize(64, 64);
        portBottomToggle.mirror = true;
        this.setRotation(portBottomToggle, 0.0F, 0.0F, 0.0F);
        ModelRenderer portFrontToggle = new ModelRenderer(this, 18, 35);
        portFrontToggle.addBox(0.0F, 0.0F, -1F, 8.0F, 8.0F, 1.0F, false);
        portFrontToggle.setRotationPoint(-4.0F, 12.0F, -8.0F);
        portFrontToggle.setTextureSize(64, 64);
        portFrontToggle.mirror = true;
        this.setRotation(portFrontToggle, 0.0F, 0.0F, 0.0F);
        ModelRenderer portLeftToggle = new ModelRenderer(this, 0, 35);
        portLeftToggle.addBox(-1F, 0.0F, 0.0F, 1.0F, 8.0F, 8.0F, false);
        portLeftToggle.setRotationPoint(-8.0F, 12.0F, -4.0F);
        portLeftToggle.setTextureSize(64, 64);
        portLeftToggle.mirror = true;
        this.setRotation(portLeftToggle, 0.0F, 0.0F, 0.0F);
        ModelRenderer portRightToggle = new ModelRenderer(this, 0, 35);
        portRightToggle.addBox(1F, 0.0F, 0.0F, 1.0F, 8.0F, 8.0F, false);
        portRightToggle.setRotationPoint(7.0F, 12.0F, -4.0F);
        portRightToggle.setTextureSize(64, 64);
        portRightToggle.mirror = true;
        this.setRotation(portRightToggle, 0.0F, 0.0F, 0.0F);
        ModelRenderer portTopToggle = new ModelRenderer(this, 0, 26);
        portTopToggle.addBox(0.0F, -1F, 0.0F, 8.0F, 1.0F, 8.0F, false);
        portTopToggle.setRotationPoint(-4.0F, 8.0F, -4.0F);
        portTopToggle.setTextureSize(64, 64);
        portTopToggle.mirror = true;
        this.setRotation(portTopToggle, 0.0F, 0.0F, 0.0F);
        this.ports = new ModelRenderer[]{portFrontToggle, portLeftToggle, portRightToggle, portBackToggle, portTopToggle, portBottomToggle};
    }

    public void renderSidesBatched(@Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight, Set<RelativeSide> enabledSides, boolean hasEffect) {
        if (!enabledSides.isEmpty()) {
            IVertexBuilder buffer = this.getVertexBuilder(renderer, this.RENDER_TYPE, hasEffect);
            Iterator var9 = enabledSides.iterator();

            RelativeSide outputSide;
            while(var9.hasNext()) {
                outputSide = (RelativeSide)var9.next();
                int sideOrdinal = outputSide.ordinal();
                this.ports[sideOrdinal].render(matrix, buffer, light, overlayLight, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

    }
}
