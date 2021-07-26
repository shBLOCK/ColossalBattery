package com.shblock.colossalbattery.helper;

import com.shblock.colossalbattery.material.BatteryMaterial;
import com.shblock.colossalbattery.material.BatteryMaterials;
import com.shblock.colossalbattery.tileentity.TileMultiBlockDummy;
import com.shblock.colossalbattery.tileentity.TileMultiBlockPartBase;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;

public class BatteryStructure extends CubeStructure {
    public final BatteryMaterial material;
    private int material_block_count = 0;
    private BlockPos core_pos;

    private int[] structure_size_cache;
    private int[] render_offset_cache;
    private AxisAlignedBB render_bounding_box_cache;
    public HashSet<BlockPos> interface_list = new HashSet<>();

    public BatteryStructure(CubeStructure cubeStructure, BatteryMaterial material) {
        super(cubeStructure.world, cubeStructure.min_pos, cubeStructure.max_pos);
        this.material = material;
//        cubeStructure.forEach(pos -> {
//            if (material.interface_validator.test(cubeStructure.world.getBlockState(pos).getBlock())) {
//                interface_list.add(pos);
//            }
//        });
    }

    public int getMaterialBlockCount() {
        if (this.material_block_count == 0) {
            this.material_block_count = getValidBlockCount(material.frame_validator.or(material.outline_validator).or(material.inner_validator));
        }
        return this.material_block_count;
    }

    public BlockPos getCorePos() {
        return this.core_pos;
    }

    public long getCapacity() {
        return this.material.calculateCapacity(getMaterialBlockCount());
    }

    public int getTransferRate() {
        return this.material.calculateTransferRate(getMaterialBlockCount());
    }

    public void initCache() {
        this.structure_size_cache = getSize();
        this.render_offset_cache = new int[] {
                min_pos.getX() - core_pos.getX(),
                min_pos.getY() - core_pos.getY(),
                min_pos.getZ() - core_pos.getZ()
        };
        this.render_bounding_box_cache = new AxisAlignedBB(min_pos.subtract(new Vector3i(1, 1, 1)), max_pos.add(new Vector3i(1, 1, 1)));
    }

    public void construct(BlockPos core_pos) {
        this.core_pos = core_pos;
        initCache();
        forEach(
                blockPos -> {
                    TileEntity tile = this.world.getTileEntity(blockPos);
                    if (!(tile instanceof TileMultiBlockPartBase)) {
                        TileMultiBlockDummy.construct(this.world, blockPos, core_pos);
                    } else {
                        ((TileMultiBlockPartBase) tile).core_pos = core_pos;
                    }
                    ((TileMultiBlockPartBase) this.world.getTileEntity(blockPos)).onConstruct();
                }
        );
    }

    public void deconstruct() {
        world.getProfiler().startSection("dc");
        forEach(
                blockPos -> {
                    TileEntity te = this.world.getTileEntity(blockPos);
                    if (te instanceof TileMultiBlockDummy) {
                        world.getProfiler().startSection("dc_dd");
                        ((TileMultiBlockDummy) te).deconstruct();
                        world.getProfiler().endSection();
                    } else if (te instanceof TileMultiBlockPartBase) {
                        ((TileMultiBlockPartBase) te).onDeconstruct();
                    }
                }
        );
        world.getProfiler().endSection();
    }

    public int[] getStructureSize() {
        return this.structure_size_cache;
    }

    public int[] getRenderOffset() {
        return this.render_offset_cache;
    }

    public AxisAlignedBB getRenderBoundingBox() {
        return this.render_bounding_box_cache;
    }

    @Override
    public CompoundNBT toNBT() {
        CompoundNBT tag = super.toNBT();
        tag.putString("type", this.material.name);
        tag.putInt("material_block_count", this.material_block_count);
        tag.put("core_pos", NBTUtil.writeBlockPos(core_pos));
        ListNBT list = new ListNBT();
        for (BlockPos pos : this.interface_list) {
            list.add(NBTUtil.writeBlockPos(pos));
        }
        tag.put("interface_list", list);
        return tag;
    }

    public static BatteryStructure fromNBT(CompoundNBT tag) {
        BatteryStructure structure = new BatteryStructure(CubeStructure.fromNBT(tag), BatteryMaterials.fromName(tag.getString("type")));
        structure.material_block_count = tag.getInt("material_block_count");
        structure.core_pos = NBTUtil.readBlockPos(tag.getCompound("core_pos"));
        structure.initCache();
        structure.interface_list.clear();
        ListNBT list = tag.getList("interface_list", Constants.NBT.TAG_COMPOUND);
        for (INBT nbt : list) {
            structure.interface_list.add(NBTUtil.readBlockPos((CompoundNBT) nbt));
        }
        return structure;
    }
}
