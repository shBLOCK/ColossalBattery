package com.shblock.colossalbattery.helper;

import com.shblock.colossalbattery.material.BatteryMaterial;
import com.shblock.colossalbattery.material.BatteryMaterials;
import com.shblock.colossalbattery.tileentity.TileMultiBlockDummy;
import com.shblock.colossalbattery.tileentity.TileMultiBlockPartBase;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class BatteryStructure extends CubeStructure {
    public final BatteryMaterial material;
    private int material_block_count = 0;

    public BatteryStructure(CubeStructure cubeStructure, BatteryMaterial material) {
        super(cubeStructure.world, cubeStructure.min_pos, cubeStructure.max_pos);
        this.material = material;
    }

    public int getMaterialBlockCount() {
        if (this.material_block_count == 0) {
            this.material_block_count = getValidBlockCount(material.outline_validator.or(material.inner_validator));
        }
        return this.material_block_count;
    }

    public long getCapacity() {
        return this.material.calculateCapacity(getMaterialBlockCount());
    }

    public int getTransferRate() {
        return this.material.calculateTransferRate(getMaterialBlockCount());
    }

    public void construct(BlockPos core_pos) {
        forEach(
                blockPos -> {
                    TileEntity tile = this.world.getTileEntity(blockPos);
                    if (!(tile instanceof TileMultiBlockPartBase)) {
                        TileMultiBlockDummy.construct(this.world, blockPos, core_pos);
                    } else {
                        ((TileMultiBlockPartBase) tile).core_pos = core_pos;
                    }
                }
        );
    }

    public void deconstruct() {
        forEach(
                blockPos -> {
                    TileEntity te = this.world.getTileEntity(blockPos);
                    if (te instanceof TileMultiBlockDummy) {
                        ((TileMultiBlockDummy) te).deconstruct();
                    }
                }
        );
    }

    @Override
    public CompoundNBT toNBT() {
        CompoundNBT tag = super.toNBT();
        tag.putString("type", this.material.name);
        tag.putInt("material_block_count", this.material_block_count);
        return tag;
    }

    public static BatteryStructure fromNBT(CompoundNBT tag) {
        BatteryStructure structure = new BatteryStructure(CubeStructure.fromNBT(tag), BatteryMaterials.fromName(tag.getString("type")));
        structure.material_block_count = tag.getInt("material_block_count");
        return structure;
    }
}
