package com.shblock.colossalbattery.tileentity;

import com.shblock.colossalbattery.RegistryEntries;
import com.shblock.colossalbattery.helper.BatteryStructure;
import com.shblock.colossalbattery.helper.MathHelper;
import com.shblock.colossalbattery.helper.MultiBlockHelper;
import com.shblock.colossalbattery.material.BatteryMaterial;
import lombok.experimental.Delegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.integrateddynamics.capability.energystorage.IEnergyStorageCapacity;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

//@Mod.EventBusSubscriber
public class TileBatteryCore extends TileMultiBlockPartBase implements IEnergyStorageCapacity, CyclopsTileEntity.ITickingTile {
    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    public static final HashSet<TileBatteryCore> collection = new HashSet<>();

    private static final AxisAlignedBB NONE = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

    private BatteryStructure structure;
    private long energy = 0;
    private long capacity = 0;
    private int transfer_rate = 0;

//    private boolean shouldSendStructureUpdate = false;

    public TileBatteryCore() {
        super(RegistryEntries.TILE_BATTERY_CORE);
        addCapabilityInternal(CapabilityEnergy.ENERGY, LazyOptional.of(() -> this));
        collection.add(this);
    }

    @Override
    public void remove() {
        super.remove();
        collection.remove(this);
    }

//    @SubscribeEvent
//    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
//        System.out.println(event);
//        if (!event.getPlayer().world.isRemote()) {
//            for (TileBatteryCore obj : collection) {
//                obj.shouldSendStructureUpdate = true;
//            }
//        }
//    }

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
//        this.shouldSendStructureUpdate = true;
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
//            this.shouldSendStructureUpdate = true;
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

    public HashSet<BlockPos> getInterfaceList() {
        return isFormed() ? this.structure.interface_list : null;
    }

    public Set<Direction> getInterfaceRenderFaces(BlockPos pos) {
        if (!isFormed()) return null;
        HashSet<Direction> result = new HashSet<>();
        if (pos.getX() == structure.min_pos.getX()) result.add(Direction.WEST);
        if (pos.getX() == structure.max_pos.getX()) result.add(Direction.EAST);
        if (pos.getY() == structure.min_pos.getY()) result.add(Direction.DOWN);
        if (pos.getY() == structure.max_pos.getY()) result.add(Direction.UP);
        if (pos.getZ() == structure.min_pos.getZ()) result.add(Direction.NORTH);
        if (pos.getZ() == structure.max_pos.getZ()) result.add(Direction.SOUTH);
        return result;
    }

    public EnumIOMode getInterfaceMode(BlockPos pos) {
        if (!isFormed()) return null;
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileBatteryInterface) {
            return ((TileBatteryInterface) tile).getMode();
        }
        return null;
    }

    public BlockPos getInterfaceOffset(BlockPos pos) {
        if (!isFormed()) return null;
        return pos.subtract(new Vector3i(this.pos.getX(), this.pos.getY(), this.pos.getZ()));
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (!isFormed()) {
            return NONE;
        }
        return this.structure.getRenderBoundingBox();
    }

    public float getEnergyPercentage() {
        if (!isFormed()) return 0F;
        return (float) getEnergy() / (float) getCapacity();
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        if (tag.contains("structure")) {
            if (!tag.getCompound("structure").isEmpty()) { //Empty means structure didn't update
                this.structure = BatteryStructure.fromNBT(tag.getCompound("structure"));
            }
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

//    @Override
//    public SUpdateTileEntityPacket getUpdatePacket() {
//        CompoundNBT tag = getUpdateTag();
//        if (this.shouldSendStructureUpdate) {
//            this.shouldSendStructureUpdate = false;
//        }
//        return new SUpdateTileEntityPacket(getPos(), 1, tag);
//    }
//
//    @Override
//    public CompoundNBT getUpdateTag() {
//        CompoundNBT tag = super.getUpdateTag();
//        if (!this.shouldSendStructureUpdate) {
//            tag.put("structure", new CompoundNBT());
//        }
//        return tag;
//    }

//    @Override
//    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
//        BatteryStructure old_structure = this.structure;
//        super.handleUpdateTag(state, tag);
//        if (!tag.contains("structure_changed")) {
//            this.structure = old_structure;
//        }
//
//    }

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

    @Override
    protected int getUpdateBackoffTicks() {
        return 5;
    }
}
