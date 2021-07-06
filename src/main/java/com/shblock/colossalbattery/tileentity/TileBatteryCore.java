package com.shblock.colossalbattery.tileentity;

import com.shblock.colossalbattery.RegistryEntries;
import com.shblock.colossalbattery.helper.BatteryStructure;
import com.shblock.colossalbattery.helper.MathHelper;
import com.shblock.colossalbattery.helper.MultiBlockHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import org.cyclops.integrateddynamics.capability.energystorage.IEnergyStorageCapacity;

public class TileBatteryCore extends TileMultiBlockPartBase implements IEnergyStorageCapacity {
    private BatteryStructure structure;
    private long energy = 0;
    private long capacity = 0;
    private int transfer_rate = 0;

    public TileBatteryCore() {
        super(RegistryEntries.TILE_BATTERY_CORE);
        addCapabilityInternal(CapabilityEnergy.ENERGY, LazyOptional.of(() -> this));
    }

    @Override
    public boolean isFormed() {
        return this.structure != null;
    }

    public boolean detectStructure() {
        BatteryStructure result = MultiBlockHelper.validateBatteryStructure(this.world, this.pos);
        if (result == null) {
            return false;
        }
        this.structure = result;
        setCapacity(this.structure.getCapacity());
        this.transfer_rate = this.structure.getTransferRate();
        this.structure.construct(this.pos);
        markDirty();
        return true;
    }

    @Override
    public void onDestroy() {
        if (this.structure != null) {
//            setCapacity(0);
            this.structure.deconstruct();
            this.structure = null;
            markDirty();
        }
    }

    public void onStructureRightClick(BlockPos click_pos, PlayerEntity player) {
        player.sendStatusMessage(new StringTextComponent(Long.toString(this.energy) + "/" + Long.toString(this.capacity)), true);
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        if (tag.contains("structure")) {
            this.structure = BatteryStructure.fromNBT(tag.getCompound("structure"));
        }
        this.energy = tag.getLong("energy");
        this.capacity = tag.getLong("capacity");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        if (this.structure != null) {
            tag.put("structure", this.structure.toNBT());
        }
        tag.putLong("energy", this.energy);
        tag.putLong("capacity", this.capacity);
        return tag;
    }

    public long getCapacity() {
        return this.capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
        markDirty();
    }

    @Override
    public void setCapacity(int capacity) {
        setCapacity((long) capacity);
    }

    public long getEnergy() {
        return this.energy;
    }

    public void setEnergy(long energy) {
        this.energy = energy;
        markDirty();
    }

    public void setEnergy(int energy) {
        setEnergy((long) energy);
    }

    public int getTransferRate() {
        return this.transfer_rate;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!isFormed()) {
            return 0;
        }
        int capacity_left = MathHelper.longToInt(this.capacity - this.energy);
        int max_transfer = Math.max(maxReceive, this.transfer_rate);
        int to_transfer = Math.min(capacity_left, max_transfer);
        if (!simulate) {
            setEnergy(this.energy + to_transfer);
        }
        return to_transfer;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!isFormed()) {
            return 0;
        }
        int energy_left = MathHelper.longToInt(this.energy);
        int max_transfer = Math.max(maxExtract, this.transfer_rate);
        int to_transfer = Math.min(energy_left, max_transfer);
        if (!simulate) {
            setEnergy(this.energy - to_transfer);
        }
        return to_transfer;
    }

    @Override
    public int getEnergyStored() {
        return MathHelper.longToInt(this.energy);
    }

    @Override
    public int getMaxEnergyStored() {
        return MathHelper.longToInt(this.capacity);
    }

    @Override
    public boolean canExtract() {
        return isFormed();
    }

    @Override
    public boolean canReceive() {
        return isFormed();
    }
}
