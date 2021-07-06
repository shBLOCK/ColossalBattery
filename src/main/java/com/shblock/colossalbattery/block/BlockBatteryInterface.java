package com.shblock.colossalbattery.block;

import com.shblock.colossalbattery.block.state.EnumIOMode;
import com.shblock.colossalbattery.tileentity.TileBatteryInterface;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class BlockBatteryInterface extends BlockMultiBlockPartBase {
    public static final EnumProperty<EnumIOMode> MODE = EnumProperty.create("mode", EnumIOMode.class);

    public BlockBatteryInterface() {
        super(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.GRAY), TileBatteryInterface::new);
        this.setDefaultState(this.getStateContainer().getBaseState().with(MODE, EnumIOMode.NORMAL));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(MODE);
        super.fillStateContainer(builder);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote()) {
            if (player.isSneaking()) {
                if (player.getHeldItem(hand).isEmpty()) {
                    int old_mode = state.get(MODE).ordinal();
                    old_mode ++;
                    if (old_mode >= 3) {
                        old_mode = 0;
                    }
                    state.with(MODE, EnumIOMode.values()[old_mode]);
                }
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }
}
