package com.shblock.colossalbattery.client.render.battery;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.shblock.colossalbattery.ColossalBattery;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Set;

//All copied from Mek's ModelEnergyCube class!
@OnlyIn(Dist.CLIENT)
public class MekRender extends Model {
    private static final ResourceLocation CUBE_TEXTURE;
    private static final ResourceLocation PORT_TEXTURE;
    private final RenderType RENDER_TYPE;
    private final RenderType RENDER_TYPE_PORT;
    private final ModelRenderer[] ports;

    static {
//        CUBE_TEXTURE = new ResourceLocation(ColossalBattery.MODID, "textures/models/mek/energy_cube.png");
        CUBE_TEXTURE = MekanismUtils.getResource(MekanismUtils.ResourceType.RENDER, "energy_cube.png");
        PORT_TEXTURE = new ResourceLocation(ColossalBattery.MODID, "textures/models/mek/energy_cube_overlay.png");
    }

//    public static void onPreTextureStitch(TextureStitchEvent.Pre event) {
//        if (event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
//            event.addSprite(CUBE_TEXTURE);
//            event.addSprite(PORT_TEXTURE);
//        }
//    }

    public MekRender() {
        super(RenderType::getEntityCutout);
        this.RENDER_TYPE = this.getRenderType(CUBE_TEXTURE);
        this.RENDER_TYPE_PORT = this.getRenderType(PORT_TEXTURE);
        this.textureWidth = 64;
        this.textureHeight = 64;
        ModelRenderer portSouthToggle = new ModelRenderer(this, 18, 35);
        portSouthToggle.addBox(0.0F, 0.0F, 1F, 8.0F, 8.0F, 1.0F, false);
        portSouthToggle.setRotationPoint(-4.0F, 12.0F, 7.0F);
        portSouthToggle.setTextureSize(64, 64);
        portSouthToggle.mirror = true;
        this.setRotation(portSouthToggle, 0.0F, 0.0F, 0.0F);
        ModelRenderer portUpToggle = new ModelRenderer(this, 0, 26);
        portUpToggle.addBox(0.0F, 1F, 0.0F, 8.0F, 1.0F, 8.0F, false);
        portUpToggle.setRotationPoint(-4.0F, 23.0F, -4.0F);
        portUpToggle.setTextureSize(64, 64);
        portUpToggle.mirror = true;
        this.setRotation(portUpToggle, 0.0F, 0.0F, 0.0F);
        ModelRenderer portNorthToggle = new ModelRenderer(this, 18, 35);
        portNorthToggle.addBox(0.0F, 0.0F, -1F, 8.0F, 8.0F, 1.0F, false);
        portNorthToggle.setRotationPoint(-4.0F, 12.0F, -8.0F);
        portNorthToggle.setTextureSize(64, 64);
        portNorthToggle.mirror = true;
        this.setRotation(portNorthToggle, 0.0F, 0.0F, 0.0F);
        ModelRenderer portWestToggle = new ModelRenderer(this, 0, 35);
        portWestToggle.addBox(-1F, 0.0F, 0.0F, 1.0F, 8.0F, 8.0F, false);
        portWestToggle.setRotationPoint(-8.0F, 12.0F, -4.0F);
        portWestToggle.setTextureSize(64, 64);
        portWestToggle.mirror = true;
        this.setRotation(portWestToggle, 0.0F, 0.0F, 0.0F);
        ModelRenderer portEastToggle = new ModelRenderer(this, 0, 35);
        portEastToggle.addBox(1F, 0.0F, 0.0F, 1.0F, 8.0F, 8.0F, false);
        portEastToggle.setRotationPoint(7.0F, 12.0F, -4.0F);
        portEastToggle.setTextureSize(64, 64);
        portEastToggle.mirror = true;
        this.setRotation(portEastToggle, 0.0F, 0.0F, 0.0F);
        ModelRenderer portDownToggle = new ModelRenderer(this, 0, 26);
        portDownToggle.addBox(0.0F, -1F, 0.0F, 8.0F, 1.0F, 8.0F, false);
        portDownToggle.setRotationPoint(-4.0F, 8.0F, -4.0F);
        portDownToggle.setTextureSize(64, 64);
        portDownToggle.mirror = true;
        this.setRotation(portDownToggle, 0.0F, 0.0F, 0.0F);
        this.ports = new ModelRenderer[]{portDownToggle, portUpToggle, portNorthToggle, portSouthToggle, portWestToggle, portEastToggle};
    }

    protected IVertexBuilder getVertexBuilder(@Nonnull IRenderTypeBuffer renderer, @Nonnull RenderType renderType, boolean hasEffect) {
        return ItemRenderer.getEntityGlintVertexBuilder(renderer, renderType, false, hasEffect);
    }

    protected void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void renderSidesBatched(@Nonnull MatrixStack matrix, @Nonnull IRenderTypeBuffer renderer, int light, int overlayLight, Set<Direction> enabledSides, float[] port_color) {
        if (!enabledSides.isEmpty()) {
            IVertexBuilder buffer = this.getVertexBuilder(renderer, this.RENDER_TYPE, false);
            Iterator<Direction> var9 = enabledSides.iterator();

            Direction outputSide;
            while(var9.hasNext()) {
                outputSide = var9.next();
                int sideOrdinal = outputSide.ordinal();
                this.ports[sideOrdinal].render(matrix, buffer, light, overlayLight, 1.0F, 1.0F, 1.0F, 1.0F);
            }

            buffer = this.getVertexBuilder(renderer, this.RENDER_TYPE_PORT, false);
            var9 = enabledSides.iterator();

            while(var9.hasNext()) {
                outputSide = var9.next();
                int sideOrdinal = outputSide.ordinal();
                this.ports[sideOrdinal].render(matrix, buffer, light, overlayLight, port_color[0], port_color[1], port_color[2], 1.0F);
            }
        }
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) { }
}
