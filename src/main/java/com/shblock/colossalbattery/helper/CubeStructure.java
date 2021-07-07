package com.shblock.colossalbattery.helper;

import com.shblock.colossalbattery.block.BlockMultiBlockDummy;
import com.shblock.colossalbattery.tileentity.TileMultiBlockDummy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class CubeStructure {
    public final World world;
    public final BlockPos min_pos;
    public final BlockPos max_pos;

    public CubeStructure(World world, BlockPos min_pos, BlockPos max_pos) {
        this.world = world;
        this.min_pos = min_pos;
        this.max_pos = max_pos;
    }

    public int[] getSize() {
        return new int[] {
            max_pos.getX() - min_pos.getX() + 1,
            max_pos.getY() - min_pos.getY() + 1,
            max_pos.getZ() - min_pos.getZ() + 1
        };
    }

    public int getBlockCount() {
        int[] size = getSize();
        return size[0] * size[1] * size[2];
    }

    public int getValidBlockCount(Predicate<Block> validator) {
        int counter = 0;
        for (int x = min_pos.getX(); x <= max_pos.getX(); x++) {
            for (int y = min_pos.getY(); y <= max_pos.getY(); y++) {
                for (int z = min_pos.getZ(); z <= max_pos.getZ(); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = this.world.getBlockState(pos).getBlock();
                    if (block instanceof BlockMultiBlockDummy) {
                        TileMultiBlockDummy tile = (TileMultiBlockDummy) this.world.getTileEntity(pos);
                        if (tile != null) {
                            BlockState blockStateIn = tile.getBlockStateIn();
                            if (blockStateIn != null) {
                                if (validator.test(blockStateIn.getBlock())) {
                                    counter++;
                                }
                            }
                        }
                    } else {
                        if (validator.test(block)) {
                            counter++;
                        }
                    }
                }
            }
        }
        return counter;
    }

    public void forEach(Consumer<BlockPos> consumer) {
        for (int x = min_pos.getX(); x <= max_pos.getX(); x++) {
            for (int y = min_pos.getY(); y <= max_pos.getY(); y++) {
                for (int z = min_pos.getZ(); z <= max_pos.getZ(); z++) {
                    consumer.accept(new BlockPos(x, y, z));
                }
            }
        }
    }

    public CompoundNBT toNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.put("dim", NBTHelper.writeWorld(this.world));
        tag.put("min_pos", NBTUtil.writeBlockPos(this.min_pos));
        tag.put("max_pos", NBTUtil.writeBlockPos(this.max_pos));
        return tag;
    }

    public static CubeStructure fromNBT(CompoundNBT tag) {
        return new CubeStructure(
                NBTHelper.readWorld(tag.getString("dim")),
                NBTUtil.readBlockPos(tag.getCompound("min_pos")),
                NBTUtil.readBlockPos(tag.getCompound("max_pos"))
        );
    }
}
