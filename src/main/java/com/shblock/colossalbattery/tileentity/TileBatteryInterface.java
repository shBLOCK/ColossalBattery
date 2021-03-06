package com.shblock.colossalbattery.tileentity;

import com.shblock.colossalbattery.RegistryEntries;
import com.shblock.colossalbattery.helper.MathHelper;
import lombok.experimental.Delegate;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

public class TileBatteryInterface extends TileMultiBlockPartBase implements IEnergyStorage, CyclopsTileEntity.ITickingTile {
    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    private EnumIOMode mode = EnumIOMode.NORMAL;

    public TileBatteryInterface() {
        super(RegistryEntries.TILE_BATTERY_INTERFACE);
        addCapabilityInternal(CapabilityEnergy.ENERGY, LazyOptional.of(() -> this));
    }

    public EnumIOMode getMode() {
        return this.mode;
    }

    public void changeMode() {
        this.mode = this.mode.getNext();
        markDirty();
        sendUpdate();
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        this.mode = EnumIOMode.getById(tag.getInt("mode"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.putInt("mode", this.mode.getId());
        return tag;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        TileBatteryCore te = getCoreTile();
        if (te != null) {
            return te.receiveEnergy(maxReceive, simulate);
        }
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        TileBatteryCore te = getCoreTile();
        if (te != null) {
            return te.extractEnergy(maxExtract, simulate);
        }
        return 0;
    }

    @Override
    public int getEnergyStored() {
        TileBatteryCore te = getCoreTile();
        if (te != null) {
            return te.getEnergyStored();
        }
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        TileBatteryCore te = getCoreTile();
        if (te != null) {
            return te.getMaxEnergyStored();
        }
        return 0;
    }

    @Override
    public boolean canExtract() {
        return isFormed();
    }

    @Override
    public boolean canReceive() {
        return isFormed();
    }

    public static LazyOptional<IEnergyStorage> getEnergyStorage(IBlockReader world, BlockPos pos, Direction facing) {
        IEnergyStorage energyStorage = TileHelpers.getCapability(world, pos, facing, CapabilityEnergy.ENERGY).orElse(null);
        return energyStorage == null ? LazyOptional.empty() : LazyOptional.of(() -> energyStorage);
    }

    private void autoOutput(TileBatteryCore core_tile) {
        if (core_tile == null) return;
        for (Direction facing : Direction.values()) {
            IEnergyStorage energyStorage = getEnergyStorage(this.world, this.pos.offset(facing), facing.getOpposite()).orElse(null);
            if (energyStorage != null) {
                if (energyStorage instanceof TileBatteryCore) {
                    if (((TileBatteryCore) energyStorage).getPos().equals(this.core_pos)) {
                        continue;
                    }
                }
                if (energyStorage instanceof TileBatteryInterface) {
                    if (((TileBatteryInterface) energyStorage).core_pos.equals(this.core_pos)) {
                        continue;
                    }
                }
                int energy = energyStorage.receiveEnergy(Math.min(core_tile.this_tick_extract_left, core_tile.getEnergyStored()), false);
                core_tile.setEnergy(core_tile.getEnergy() - energy);
            }
        }
    }

    private void autoInput(TileBatteryCore core_tile) {
        if (core_tile == null) return;
        for (Direction facing : Direction.values()) {
            IEnergyStorage energyStorage = getEnergyStorage(this.world, this.pos.offset(facing), facing.getOpposite()).orElse(null);
            if (energyStorage != null) {
                if (energyStorage instanceof TileBatteryCore) {
                    if (((TileBatteryCore) energyStorage).getPos().equals(this.core_pos)) {
                        continue;
                    }
                }
                if (energyStorage instanceof TileBatteryInterface) {
                    if (((TileBatteryInterface) energyStorage).core_pos.equals(this.core_pos)) {
                        continue;
                    }
                }
                int energy = energyStorage.extractEnergy(Math.min(core_tile.this_tick_receive_left, MathHelper.longToInt(core_tile.getCapacity() - core_tile.getEnergy())), false);
                core_tile.setEnergy(core_tile.getEnergy() + energy);
            }
        }
    }

    @Override
    public void tick() {
        if (!this.world.isRemote()) {
            if (isFormed()) {
                switch (this.mode) {
                    case NORMAL:
                        break;
                    case INPUT:
                        autoInput(getCoreTile());
                        break;
                    case OUTPUT:
                        autoOutput(getCoreTile());
                        break;
                }
            }
        }
        this.tickingTileComponent.tick();
    }
}
