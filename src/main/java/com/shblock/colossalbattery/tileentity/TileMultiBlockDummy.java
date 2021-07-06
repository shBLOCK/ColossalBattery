package com.shblock.colossalbattery.tileentity;

import com.shblock.colossalbattery.ColossalBattery;
import com.shblock.colossalbattery.RegistryEntries;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public class TileMultiBlockDummy extends TileMultiBlockPartBase {
    private BlockState blockState;

    public TileMultiBlockDummy() {
        super(RegistryEntries.TILE_MULTI_BLOCK_DUMMY);
    }

    public void setBlockStateIn(BlockState blockState) {
        this.blockState = blockState;
        markDirty();
    }

    public BlockState getBlockStateIn() {
        return this.blockState;
    }

    public static void construct(World world, BlockPos pos, BlockPos core_pos) {
        TileMultiBlockDummy tile = new TileMultiBlockDummy();
        tile.setBlockStateIn(world.getBlockState(pos));
        tile.core_pos = core_pos;
        tile.markDirty();
        world.setBlockState(pos, RegistryEntries.BLOCK_MULTI_BLOCK_DUMMY.getDefaultState());
        world.setTileEntity(pos, tile);
    }

    public void deconstruct() {
        if (this.blockState == null) {
            ColossalBattery.clog(Level.WARN, "Multi block dummy block found null BlockState when deconstruct! Will replace with air.");
            this.world.setBlockState(this.pos, Blocks.AIR.getDefaultState());
        }
        this.world.setBlockState(this.pos, this.blockState);
        this.world.removeTileEntity(this.pos);
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        if (tag.contains("blockState")) {
            this.blockState = NBTUtil.readBlockState(tag.getCompound("blockState"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        if (this.blockState != null) {
            tag.put("blockState", NBTUtil.writeBlockState(this.blockState));
        }
        return tag;
    }
}
