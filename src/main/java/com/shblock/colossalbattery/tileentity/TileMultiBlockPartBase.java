package com.shblock.colossalbattery.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

public class TileMultiBlockPartBase extends CyclopsTileEntity {
    public BlockPos core_pos;

    public TileMultiBlockPartBase(TileEntityType<?> type) {
        super(type);
    }

    public boolean isFormed() {
        return this.core_pos != null;
    }

    public TileBatteryCore getCoreTile() {
        TileEntity te = this.world.getTileEntity(this.core_pos);
        if (te instanceof TileBatteryCore) {
            return (TileBatteryCore) te;
        }
        return null;
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        if (tag.contains("core_pos")) {
            this.core_pos = NBTUtil.readBlockPos(tag.getCompound("core_pos"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        if (isFormed()) {
            tag.put("core_pos", NBTUtil.writeBlockPos(this.core_pos));
        }
        return tag;
    }

    public void onDestroy() {
        if (this.world.isRemote) return;
        if (!isFormed()) return;
        TileEntity tile_core = this.world.getTileEntity(this.core_pos);
        if (tile_core instanceof TileBatteryCore) {
            ((TileBatteryCore) tile_core).onDestroy();
        }
    }
}
