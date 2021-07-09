package com.shblock.colossalbattery.tileentity;

import com.shblock.colossalbattery.RegistryEntries;
import com.shblock.colossalbattery.helper.BatteryStructure;
import com.shblock.colossalbattery.helper.MathHelper;
import com.shblock.colossalbattery.helper.MultiBlockHelper;
import com.shblock.colossalbattery.material.BatteryMaterial;
import lombok.experimental.Delegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.integrateddynamics.capability.energystorage.IEnergyStorageCapacity;

import javax.annotation.Nullable;

public class TileBatteryCore extends TileMultiBlockPartBase implements IEnergyStorageCapacity, CyclopsTileEntity.ITickingTile {
    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    private static final AxisAlignedBB NONE = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

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

    public boolean detectStructure(@Nullable PlayerEntity player) {
        BatteryStructure result = MultiBlockHelper.validateBatteryStructure(this.world, this.pos, player);
        if (result == null) {
            return false;
        }
        this.structure = result;
        setCapacity(this.structure.getCapacity());
        this.transfer_rate = this.structure.getTransferRate();
        this.structure.construct(this.pos);
        markDirty();
        sendUpdate();
        return true;
    }

    @Override
    public void deconstructStructure() {
        if (this.structure != null) {
//            setCapacity(0);
            this.structure.deconstruct();
            this.structure = null;
            this.core_pos = null;
            markDirty();
            sendUpdate();
        }
    }

    public void onStructureRightClick(BlockPos click_pos, PlayerEntity player) {
        player.sendStatusMessage(new StringTextComponent(Long.toString(this.energy) + "/" + Long.toString(this.capacity)), true);
    }

    @Override
    public TileBatteryCore getCoreTile() {
        return this;
    }

    public int[] getStructureSize() {
        if (!isFormed()) {
            return null;
        }
        return this.structure.getStructureSize();
    }

    public int[] getRenderOffset() {
        if (!isFormed()) {
            return null;
        }
        return this.structure.getRenderOffset();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (!isFormed()) {
            return NONE;
        }
        return this.structure.getRenderBoundingBox();
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        if (tag.contains("structure")) {
            this.structure = BatteryStructure.fromNBT(tag.getCompound("structure"));
        } else {
            this.structure = null;
        }
        this.energy = tag.getLong("energy");
        this.capacity = tag.getLong("capacity");
        this.transfer_rate = tag.getInt("transfer_rate");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        if (this.structure != null) {
            tag.put("structure", this.structure.toNBT());
        }
        tag.putLong("energy", this.energy);
        tag.putLong("capacity", this.capacity);
        tag.putInt("transfer_rate", this.transfer_rate);
        return tag;
    }

    public long getCapacity() {
        return this.capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
        markDirty();
        sendUpdate();
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
        sendUpdate();
    }

    public void setEnergy(int energy) {
        setEnergy((long) energy);
    }

    public int getTransferRate() {
        return this.transfer_rate;
    }

    public BatteryMaterial getMaterial() {
        if (this.structure == null) return null;
        return this.structure.material;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!isFormed()) {
            return 0;
        }
//        int capacity_left = MathHelper.longToInt(this.capacity - this.energy);
        long capacity_left = this.capacity - this.energy;
        long max_transfer = Math.max(maxReceive, this.transfer_rate);
        long to_transfer = Math.min(capacity_left, max_transfer);
        int int_to_transfer = MathHelper.longToInt(to_transfer);
        if (!simulate) {
            setEnergy(this.energy + int_to_transfer);
        }
        return int_to_transfer;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!isFormed()) {
            return 0;
        }
//        int energy_left = MathHelper.longToInt(this.energy);
        long energy_left = this.energy;
        long max_transfer = Math.max(maxExtract, this.transfer_rate);
        long to_transfer = Math.min(energy_left, max_transfer);
        int int_to_transfer = MathHelper.longToInt(to_transfer);
        if (!simulate) {
            setEnergy(this.energy - int_to_transfer);
        }
        return int_to_transfer;
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

    @Override
    public void tick() {
        this.tickingTileComponent.tick();
    }
}
