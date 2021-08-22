package com.shblock.colossalbattery.block;

import com.shblock.colossalbattery.ColossalBattery;
import com.shblock.colossalbattery.tileentity.TileBatteryCore;
import com.shblock.colossalbattery.tileentity.TileMultiBlockPartBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

import java.util.function.Supplier;

public class BlockMultiBlockPartBase extends BlockTile {
    public static final BooleanProperty FORMED = BooleanProperty.create("formed");

    public BlockMultiBlockPartBase(Properties properties, Supplier<CyclopsTileEntity> tileEntitySupplier) {
        super(properties, tileEntitySupplier);
        this.setDefaultState(this.getStateContainer().getBaseState().with(FORMED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FORMED);
        super.fillStateContainer(builder);
    }

    public void setFormed(World world, BlockPos pos, boolean formed) {
        BlockState bs = world.getBlockState(pos);
        if (bs.get(FORMED) != formed) {
            world.setBlockState(pos, bs.with(FORMED, formed));
            world.notifyNeighborsOfStateChange(pos, getBlock());
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote()) {
            if (!player.isSneaking() && player.getHeldItem(hand).isEmpty()) {
                TileMultiBlockPartBase tile = (TileMultiBlockPartBase) world.getTileEntity(pos);
                if (tile != null) {
                    if (tile instanceof TileBatteryCore) {
                        ((TileBatteryCore) tile).onStructureRightClick(pos, player);
                        return ActionResultType.SUCCESS;
                    } else if (tile.isFormed()) {
                        TileEntity core_tile = world.getTileEntity(tile.core_pos);
                        if (core_tile instanceof TileBatteryCore) {
                            ((TileBatteryCore) core_tile).onStructureRightClick(pos, player);
                        }
                        return ActionResultType.SUCCESS;
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
                tile.deconstructStructure();
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

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return state.get(FORMED) ? BlockRenderType.INVISIBLE : super.getRenderType(state);
    }

    @Override
    public float getExplosionResistance(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) {
        if (state.get(FORMED)) {
            TileMultiBlockPartBase tile = (TileMultiBlockPartBase) world.getTileEntity(pos);
            if (tile != null) {
                TileBatteryCore core_tile = tile.getCoreTile();
                if (core_tile != null) {
                    return core_tile.getMaterial().isExplosionResistance() ? Float.MAX_VALUE : 0;
                }
            }
        }
        return 0;
    }
}
