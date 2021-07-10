package com.shblock.colossalbattery.client.render.battery;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.shblock.colossalbattery.ColossalBattery;
import com.shblock.colossalbattery.material.BatteryMaterial;
import com.shblock.colossalbattery.material.BatteryMaterials;
import com.shblock.colossalbattery.tileentity.EnumIOMode;
import com.shblock.colossalbattery.tileentity.TileBatteryCore;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class RenderBattery6Face extends RenderBatteryBase {
    public static final RenderBattery6Face _instance = new RenderBattery6Face();

    private static final float OFFSET = 0.001F;
    private static final float MINY = 0F;
    private static final float MAXY = 1F;
    private static final float MIN = 0F - OFFSET;
    private static final float MAX = 1F + OFFSET;
    private static final float[][][] coordinates = {
            { // DOWN
                    {MIN, MINY, MIN},
                    {MIN, MINY, MAX},
                    {MAX, MINY, MAX},
                    {MAX, MINY, MIN}
            },
            { // UP
                    {MIN, MAXY, MIN},
                    {MIN, MAXY, MAX},
                    {MAX, MAXY, MAX},
                    {MAX, MAXY, MIN}
            },
            { // NORTH
                    {MIN, MINY, MIN},
                    {MIN, MAXY, MIN},
                    {MAX, MAXY, MIN},
                    {MAX, MINY, MIN}
            },
            { // SOUTH
                    {MAX, MINY, MAX},
                    {MAX, MAXY, MAX},
                    {MIN, MAXY, MAX},
                    {MIN, MINY, MAX}
            },
            { // WEST
                    {MIN, MINY, MAX},
                    {MIN, MAXY, MAX},
                    {MIN, MAXY, MIN},
                    {MIN, MINY, MIN}
            },
            { // EAST
                    {MAX, MINY, MIN},
                    {MAX, MAXY, MIN},
                    {MAX, MAXY, MAX},
                    {MAX, MINY, MAX}
            }
    };

    private static final HashMap<BatteryMaterial, ResourceLocation> TEXTURES_BATTERY = new HashMap<>();
    private static final HashMap<BatteryMaterial, ResourceLocation> ICONS_BATTERY = new HashMap<>();
    private static final ResourceLocation NODES_TEXTURE = new ResourceLocation(ColossalBattery.MODID, "block/interface_nodes");
    private static final HashMap<Direction, Quaternion> ROTATION_MAP = new HashMap<>();
    static {
        String start = "models/battery/";
        String end = "";
        TEXTURES_BATTERY.put(BatteryMaterials.COBBLESTONE, new ResourceLocation(ColossalBattery.MODID, start + "cobblestone" + end));
        TEXTURES_BATTERY.put(BatteryMaterials.COPPER, new ResourceLocation(ColossalBattery.MODID, start + "copper" + end));
        TEXTURES_BATTERY.put(BatteryMaterials.IRON, new ResourceLocation(ColossalBattery.MODID, start + "iron" + end));
        TEXTURES_BATTERY.put(BatteryMaterials.SILVER, new ResourceLocation(ColossalBattery.MODID, start + "silver" + end));
        TEXTURES_BATTERY.put(BatteryMaterials.GOLD, new ResourceLocation(ColossalBattery.MODID, start + "gold" + end));
        TEXTURES_BATTERY.put(BatteryMaterials.DIAMOND, new ResourceLocation(ColossalBattery.MODID, start + "diamond" + end));
        TEXTURES_BATTERY.put(BatteryMaterials.OBSIDIAN, new ResourceLocation(ColossalBattery.MODID, start + "obsidian" + end));
        TEXTURES_BATTERY.put(BatteryMaterials.MENRIL, new ResourceLocation(ColossalBattery.MODID, start + "menril" + end));
        TEXTURES_BATTERY.put(BatteryMaterials.ULTIMATE, new ResourceLocation(ColossalBattery.MODID, start + "ultimate" + end));

        start = "textures/models/battery/";
        end = ".png";
        ICONS_BATTERY.put(BatteryMaterials.COBBLESTONE, new ResourceLocation(ColossalBattery.MODID, start + "cobblestone" + end));
        ICONS_BATTERY.put(BatteryMaterials.COPPER, new ResourceLocation(ColossalBattery.MODID, start + "copper" + end));
        ICONS_BATTERY.put(BatteryMaterials.IRON, new ResourceLocation(ColossalBattery.MODID, start + "iron" + end));
        ICONS_BATTERY.put(BatteryMaterials.SILVER, new ResourceLocation(ColossalBattery.MODID, start + "silver" + end));
        ICONS_BATTERY.put(BatteryMaterials.GOLD, new ResourceLocation(ColossalBattery.MODID, start + "gold" + end));
        ICONS_BATTERY.put(BatteryMaterials.DIAMOND, new ResourceLocation(ColossalBattery.MODID, start + "diamond" + end));
        ICONS_BATTERY.put(BatteryMaterials.OBSIDIAN, new ResourceLocation(ColossalBattery.MODID, start + "obsidian" + end));
        ICONS_BATTERY.put(BatteryMaterials.MENRIL, new ResourceLocation(ColossalBattery.MODID, start + "menril" + end));
        ICONS_BATTERY.put(BatteryMaterials.ULTIMATE, new ResourceLocation(ColossalBattery.MODID, start + "ultimate" + end));

        ROTATION_MAP.put(Direction.DOWN, new Quaternion(180, 0, 0, true));
        ROTATION_MAP.put(Direction.UP, new Quaternion(0, 0, 0, true));
        ROTATION_MAP.put(Direction.NORTH, new Quaternion(-90, 0, 0, true));
        ROTATION_MAP.put(Direction.SOUTH, new Quaternion(90, 0, 0, true));
        ROTATION_MAP.put(Direction.WEST, new Quaternion(0, 0, 90, true));
        ROTATION_MAP.put(Direction.EAST, new Quaternion(0, 0, -90, true));
    }
    private final ModelRenderer main_model;
    private final ModelRenderer[] node_renders;

    public RenderBattery6Face() {
        this.main_model = new ModelRenderer(64, 64, 0, 0);
        this.main_model.addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F);
        ArrayList<ModelRenderer> renders = new ArrayList<>();
        for (int i=0;i<4;i++) {
            ModelRenderer model = new ModelRenderer(32, 32, 0, i * 8);
            model.addBox(-2.0F, 8F, -2.0F, 4.0F, 1.0F, 4.0F);
            renders.add(model);
        }
        this.node_renders = renders.toArray(new ModelRenderer[4]);
    }

    public static void onPreTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
            for (ResourceLocation value : TEXTURES_BATTERY.values()) {
                event.addSprite(value);
            }
            event.addSprite(NODES_TEXTURE);
        }
    }

    @Override
    public void render(TileBatteryCore tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.push();
        RenderMaterial material = new RenderMaterial(Atlases.CHEST_ATLAS, TEXTURES_BATTERY.get(tileEntity.getMaterial()));
        IVertexBuilder vertexBuilder = material.getBuffer(buffer, RenderType::getEntityCutout);
        this.main_model.render(matrixStack, vertexBuilder, LightTexture.packLight(15, 15), OverlayTexture.NO_OVERLAY);
        renderEnergyBar(tileEntity, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
        matrixStack.pop();
    }

    private void renderEnergyBar(TileBatteryCore tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if(tile.getEnergy() > 0) {
            float height = tile.getEnergyPercentage();
            height = (height * 12 / 16) + 0.125F;

            for(Direction side : Direction.Plane.HORIZONTAL) {
                ResourceLocation icon = ICONS_BATTERY.get(tile.getMaterial());

                float[][] c = coordinates[side.ordinal()];
                float minU = side.getHorizontalIndex() * 0.25F;
                float maxU = minU + 0.25F;
                float minV = 0.75F + 0.25F * (1.0F - height);
                float maxV = 1.0F;

                IVertexBuilder vb = buffer.getBuffer(RenderType.getText(icon));
                Matrix4f matrix = matrixStack.getLast().getMatrix();
                vb.pos(matrix, c[0][0], c[0][1] * height, c[0][2]).color(1.0F, 1.0F, 1.0F, 1.0F).tex(minU, maxV).lightmap(LightTexture.packLight(15, 15)).endVertex();
                vb.pos(matrix, c[1][0], c[1][1] * height, c[1][2]).color(1.0F, 1.0F, 1.0F, 1.0F).tex(minU, minV).lightmap(LightTexture.packLight(15, 15)).endVertex();
                vb.pos(matrix, c[2][0], c[2][1] * height, c[2][2]).color(1.0F, 1.0F, 1.0F, 1.0F).tex(maxU, minV).lightmap(LightTexture.packLight(15, 15)).endVertex();
                vb.pos(matrix, c[3][0], c[3][1] * height, c[3][2]).color(1.0F, 1.0F, 1.0F, 1.0F).tex(maxU, maxV).lightmap(LightTexture.packLight(15, 15)).endVertex();
            }
        }
    }

    @Override
    public void renderCore(Set<Direction> sides, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        RenderMaterial material = new RenderMaterial(Atlases.CHEST_ATLAS, NODES_TEXTURE);
        IVertexBuilder vertexBuilder = material.getBuffer(buffer, RenderType::getEntityCutout);
        for (Direction facing : sides) {
            renderNode(facing, this.node_renders[3], partialTicks, matrixStack, buffer, vertexBuilder, combinedLight, combinedOverlay);
        }
    }

    @Override
    public void renderInterface(Set<Direction> sides, EnumIOMode mode, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        RenderMaterial material = new RenderMaterial(Atlases.CHEST_ATLAS, NODES_TEXTURE);
        IVertexBuilder vertexBuilder = material.getBuffer(buffer, RenderType::getEntityCutout);
        for (Direction facing : sides) {
            renderNode(facing, this.node_renders[mode.getId()], partialTicks, matrixStack, buffer, vertexBuilder, combinedLight, combinedOverlay);
        }
    }

    private void renderNode(Direction facing, ModelRenderer model, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, IVertexBuilder vertexBuilder, int combinedLight, int combinedOverlay) {
        matrixStack.push();
        matrixStack.translate(0.5F, 0.5F, 0.5F);
        rotateToNodeFacing(matrixStack, facing);
        model.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
        matrixStack.pop();
    }

    private MatrixStack rotateToNodeFacing(MatrixStack matrixStack, Direction facing) {
        matrixStack.rotate(ROTATION_MAP.get(facing));
        return matrixStack;
    }
}
