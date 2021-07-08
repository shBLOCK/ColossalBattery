package com.shblock.colossalbattery.tileentity;

import com.shblock.colossalbattery.RegistryEntries;
import com.shblock.colossalbattery.block.BlockBatteryInterface;
import com.shblock.colossalbattery.helper.MathHelper;
import lombok.experimental.Delegate;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.cyclops.colossalchests.tileentity.TileInterface;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.integrateddynamics.capability.energystorage.IEnergyStorageCapacity;
import org.cyclops.integrateddynamics.core.helper.EnergyHelpers;

public class TileBatteryInterface extends TileMultiBlockPartBase implements IEnergyStorageCapacity, CyclopsTileEntity.ITickingTile {
    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    public TileBatteryInterface() {
        super(RegistryEntries.TILE_BATTERY_INTERFACE);
        addCapabilityInternal(CapabilityEnergy.ENERGY, LazyOptional.of(() -> this));
    }

    @Override
    public void setCapacity(int capacity) {
        TileBatteryCore te = getCoreTile();
        if (te != null) {
            te.setCapacity(capacity);
        }
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

    private void autoOutput(TileBatteryCore core_tile) {
        if (core_tile == null) return;
        for (Direction facing : Direction.values()) {
            IEnergyStorage energyStorage = EnergyHelpers.getEnergyStorage(this.world, this.pos.offset(facing), facing.getOpposite()).orElse(null);
            if (energyStorage != null) {
                int energy = energyStorage.receiveEnergy(Math.min(core_tile.getTransferRate(), core_tile.getEnergyStored()), false);
                core_tile.setEnergy(core_tile.getEnergy() - energy);
            }
        }
    }

    private void autoInput(TileBatteryCore core_tile) {
        if (core_tile == null) return;
        for (Direction facing : Direction.values()) {
            IEnergyStorage energyStorage = EnergyHelpers.getEnergyStorage(this.world, this.pos.offset(facing), facing.getOpposite()).orElse(null);
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
                int energy = energyStorage.extractEnergy(Math.min(core_tile.getTransferRate(), MathHelper.longToInt(core_tile.getCapacity() - core_tile.getEnergy())), false);
                core_tile.setEnergy(core_tile.getEnergy() + energy);
            }
        }
    }

    @Override
    public void tick() {
        if (!this.world.isRemote()) {
            if (isFormed()) {
                switch (getBlockState().get(BlockBatteryInterface.MODE)) {
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
