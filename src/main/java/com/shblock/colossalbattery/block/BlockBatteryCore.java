package com.shblock.colossalbattery.block;

import com.shblock.colossalbattery.tileentity.TileBatteryCore;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class BlockBatteryCore extends BlockMultiBlockPartBase {
    public BlockBatteryCore() {
        super(
                AbstractBlock.Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY)
                        .notSolid()
                        .setOpaque((blockState, world, pos) -> false)
                        .hardnessAndResistance(5.0F)
                        .harvestLevel(0),
                TileBatteryCore::new
        );
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote()) {
            if (!player.isSneaking() && player.getHeldItem(hand).isEmpty()) {
                TileBatteryCore tile = (TileBatteryCore) world.getTileEntity(pos);
                if (tile != null) {
                    if (!tile.isFormed()) {
                        if (tile.detectStructure(player)) {
                            player.sendStatusMessage(new TranslationTextComponent("message.colossal_battery.success"), false);
                        }
                        return ActionResultType.SUCCESS;
                    }
                }
            }
        } else {
            if (!player.isSneaking() && player.getHeldItem(hand).isEmpty()) {
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }
}
