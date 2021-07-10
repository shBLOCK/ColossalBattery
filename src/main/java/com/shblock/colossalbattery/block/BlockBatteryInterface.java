package com.shblock.colossalbattery.block;

import com.shblock.colossalbattery.tileentity.TileBatteryInterface;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.TileHelpers;

public class BlockBatteryInterface extends BlockMultiBlockPartBase {
    public BlockBatteryInterface() {
        super(
                AbstractBlock.Properties.create(Material.ROCK, MaterialColor.GRAY)
                        .notSolid()
                        .setOpaque((blockState, world, pos) -> false)
                        .hardnessAndResistance(5.0F)
                        .harvestLevel(0),
                TileBatteryInterface::new
        );
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote()) {
            if (player.isSneaking() && player.getHeldItem(hand).isEmpty()) {
                TileHelpers.getSafeTile(world, pos, TileBatteryInterface.class).ifPresent(TileBatteryInterface::changeMode);
                return ActionResultType.SUCCESS;
            }
        } else {
            if (player.isSneaking() && player.getHeldItem(hand).isEmpty()) {
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }
}
