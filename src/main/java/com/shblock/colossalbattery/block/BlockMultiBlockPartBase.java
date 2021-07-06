package com.shblock.colossalbattery.block;

import com.shblock.colossalbattery.tileentity.TileBatteryCore;
import com.shblock.colossalbattery.tileentity.TileMultiBlockPartBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

import java.util.function.Supplier;

public class BlockMultiBlockPartBase extends BlockTile {
    public BlockMultiBlockPartBase(Properties properties, Supplier<CyclopsTileEntity> tileEntitySupplier) {
        super(properties, tileEntitySupplier);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote()) {
            if (!player.isSneaking()) {
                TileMultiBlockPartBase tile = (TileMultiBlockPartBase) world.getTileEntity(pos);
                if (tile != null) {
                    if (tile instanceof TileBatteryCore) {
                        ((TileBatteryCore) tile).onStructureRightClick(pos, player);
                    } else if (tile.isFormed()) {
                        TileEntity core_tile = world.getTileEntity(tile.core_pos);
                        if (core_tile instanceof TileBatteryCore) {
                            ((TileBatteryCore) core_tile).onStructureRightClick(pos, player);
                        }
                    }
                }
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }

    public void onDestroy(IWorld world, BlockPos pos) {
        if (!world.isRemote()) {
            TileMultiBlockPartBase tile = (TileMultiBlockPartBase) world.getTileEntity(pos);
            if (tile != null) {
                tile.onDestroy();
            }
        }
    }

    @Override
    public void onPlayerDestroy(IWorld world, BlockPos pos, BlockState state) {
        onDestroy(world, pos);
        super.onPlayerDestroy(world, pos, state);
    }

    @Override
    public void onExplosionDestroy(World world, BlockPos pos, Explosion explosionIn) {
        onDestroy(world, pos);
        super.onExplosionDestroy(world, pos, explosionIn);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        onDestroy(world, pos);
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState blockState, PlayerEntity player) {
        onDestroy(world, pos);
        super.onBlockHarvested(world, pos, blockState, player);
    }

//    @Override
//    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
//        onDestroy(world, pos);
//        super.onReplaced(state, world, pos, newState, isMoving);
//    }
}
