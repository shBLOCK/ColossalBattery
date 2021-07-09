package com.shblock.colossalbattery.helper;

import com.shblock.colossalbattery.ColossalBattery;
import com.shblock.colossalbattery.GeneralConfig;
import com.shblock.colossalbattery.material.BatteryMaterial;
import com.shblock.colossalbattery.material.BatteryMaterials;
import com.shblock.colossalbattery.tileentity.TileBatteryCore;
import com.shblock.colossalbattery.tileentity.TileMultiBlockPartBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MultiBlockHelper {
    private static ArrayList<IFormattableTextComponent> error_list = new ArrayList<>();
    
    /**
     * Find valid target blocks that's connected with the starting pos with valid connect blocks or another valid target block.
     * @param world World in.
     * @param pos Starting pos.
     * @param validator_connect The block validator for connect blocks.
     * @param validator The block validator for target blocks.
     * @param set Scanned blocks (for recursion).
     * @param founded_set Founded blocks (for recursion and result).
     * @return The current founded core blocks.
     */
    private static HashSet<BlockPos> findConnectedBlocks(World world, BlockPos pos, Predicate<Block> validator_connect, Predicate<Block> validator, HashSet<BlockPos> set, HashSet<BlockPos> founded_set) {
        Block block = world.getBlockState(pos).getBlock();
        if (validator.test(block)) {
            founded_set.add(pos);
        }
        if (validator_connect.test(block)) {
            set.add(pos);
            for (Direction direction : Direction.values()) {
                BlockPos offset_pos = pos.offset(direction);
                if (!set.contains(offset_pos)) {
                    founded_set = findConnectedBlocks(world, offset_pos, validator_connect, validator, set, founded_set);
                }
            }
        }
        return founded_set;
    }

    /**
     * Find a single block in the structure (if no is found or multiple is found, will return null).
     * @param world World in.
     * @param pos Starting pos.
     * @param validator_connect The block validator for connect blocks.
     * @param validator The target block validator.
     * @return The founded core block (if no is found or multiple is found, will return null).
     */
    public static BlockPos findBlockSingle(World world, BlockPos pos, Predicate<Block> validator_connect, Predicate<Block> validator) {
        HashSet<BlockPos> core_set = findConnectedBlocks(world, pos, validator_connect, validator, new HashSet<>(), new HashSet<>());
        return core_set.size() != 1 ? null : core_set.iterator().next();
    }

    /**
     * Find all target blocks in the structure.
     * @param world World in.
     * @param pos Starting pos.
     * @param validator_connect The block validator for connect blocks.
     * @param validator The target block validator.
     * @return The founded blocks.
     */
    public static HashSet<BlockPos> findBlockMultiple(World world, BlockPos pos, Predicate<Block> validator_connect, Predicate<Block> validator) {
        return findConnectedBlocks(world, pos, validator_connect, validator, new HashSet<>(), new HashSet<>());
    }

    /**
     * Scan a list of all the blocks connected together that is valid (using recursion).
     * @param world World to scan in.
     * @param pos Starting pos.
     * @param validator The block validator.
     * @param set Last block set (for recursion).
     * @return The set of valid connected blocks.
     */
    private static HashSet<BlockPos> scanConnectedBlocks(World world, BlockPos pos, Predicate<Block> validator, HashSet<BlockPos> set) {
        if (validator.test(world.getBlockState(pos).getBlock())) {
            set.add(pos);
            for (Direction direction : Direction.values()) {
                BlockPos offset_pos = pos.offset(direction);
                if (!set.contains(offset_pos)) {
                    set = scanConnectedBlocks(world, offset_pos, validator, set);
                }
            }
        }
        return set;
    }

    /**
     * Scan a list of all the blocks connected together that is valid (using recursion).
     * @param world World to scan in.
     * @param pos The start pos.
     * @param validator The block validator.
     * @return The set of valid connected blocks.
     */
    public static HashSet<BlockPos> scanConnectedBlocks(World world, BlockPos pos, Predicate<Block> validator) {
        return scanConnectedBlocks(world, pos, validator, new HashSet<>());
    }

    /**
     * Find the min BlockPos (min X,Y,Z) from a set of BlockPos.
     * @param set The BlockPos set.
     * @return The min BlockPos in the set.
     */
    public static BlockPos findMinCorner(HashSet<BlockPos> set) {
        BlockPos min_pos = null;
        for (BlockPos pos : set) {
            if (min_pos == null) {
                min_pos = pos;
            } else {
                if (pos.getX() <= min_pos.getX() && pos.getY() <= min_pos.getY() && pos.getZ() <= min_pos.getZ()) {
                    min_pos = pos;
                }
            }
        }
        return min_pos;
    }

    /**
     * Find the max BlockPos (max X,Y,Z) from a set of BlockPos.
     * @param set The BlockPos set.
     * @return The max BlockPos in the set.
     */
    public static BlockPos findMaxCorner(HashSet<BlockPos> set) {
        BlockPos min_pos = null;
        for (BlockPos pos : set) {
            if (min_pos == null) {
                min_pos = pos;
            } else {
                if (pos.getX() >= min_pos.getX() && pos.getY() >= min_pos.getY() && pos.getZ() >= min_pos.getZ()) {
                    min_pos = pos;
                }
            }
        }
        return min_pos;
    }

    /**
     * Check if the pos is on the outline of the box that's defined by min pos and max pos.
     * @param min_pos Min pos of the box.
     * @param max_pos Max pos of the box.
     * @param pos The pos to check.
     * @return Is the pos on the outline of the box.
     */
    public static boolean isOutline(BlockPos min_pos, BlockPos max_pos, BlockPos pos) {
        return  pos.getX() == min_pos.getX() ||
                pos.getX() == max_pos.getX() ||
                pos.getY() == min_pos.getY() ||
                pos.getY() == max_pos.getY() ||
                pos.getZ() == min_pos.getZ() ||
                pos.getZ() == max_pos.getZ();
    }

    /**
     * Check if the pos is on the frame of the box that's defined by min pos and max pos.
     * @param min_pos Min pos of the box.
     * @param max_pos Max pos of the box.
     * @param pos The pos to check.
     * @return Is the pos on the frame of the box.
     */
    public static boolean isFrame(BlockPos min_pos, BlockPos max_pos, BlockPos pos) {
        int count = 0;
        if (pos.getX() == min_pos.getX() || pos.getX() == max_pos.getX()) count ++;
        if (pos.getY() == min_pos.getY() || pos.getY() == max_pos.getY()) count ++;
        if (pos.getZ() == min_pos.getZ() || pos.getZ() == max_pos.getZ()) count ++;
        return count >= 2;
    }

    /**
     * Get the min length of the X,Y,Z length of the box.
     * @param min_pos Min pos of the box.
     * @param max_pos Max pos of the box.
     * @return The min length.
     */
    public static int getMinSize(BlockPos min_pos, BlockPos max_pos) {
        int xl = max_pos.getX() - min_pos.getX();
        int yl = max_pos.getY() - min_pos.getY();
        int zl = max_pos.getZ() - min_pos.getZ();
        return Math.min(xl, Math.min(yl, zl)); //just min(xl, yl, zl)...
    }

    /**
     * Get the max length of the X,Y,Z length of the box.
     * @param min_pos Min pos of the box.
     * @param max_pos Max pos of the box.
     * @return The min length.
     */
    public static int getMaxSize(BlockPos min_pos, BlockPos max_pos) {
        int xl = max_pos.getX() - min_pos.getX();
        int yl = max_pos.getY() - min_pos.getY();
        int zl = max_pos.getZ() - min_pos.getZ();
        return Math.max(xl, Math.max(yl, zl)); //just max(xl, yl, zl)...
    }

    /**
     * Try find a cube structure with valid outline and inner blocks from a starting pos.
     * @param world World in.
     * @param pos Starting pos.
     * @param validator_outline The block validator for outline block.
     * @param validator_inner The block validator for core block.
     * @param validator_must_single Block that's not allowed to have multiple in the structure.
     * @return The CubeStructure object, or null if didn't found any structure.
     */
    public static CubeStructure validateBoxStructureWithCore(World world, BlockPos pos, Predicate<Block> validator_frame, Predicate<Block> validator_outline, Predicate<Block> validator_inner, Predicate<Block> validator_must_single, TranslationTextComponent material_name) {
        HashSet<BlockPos> outline_set;
        try {
            outline_set = scanConnectedBlocks(world, pos, validator_outline.or(validator_frame));
        } catch (StackOverflowError ignored) {
            error_list.add(new TranslationTextComponent("message.colossal_battery.error.too_much_block").append(material_name));
            ColossalBattery.clog(Level.WARN, "A structure have too much connected blocks caused StackOverflowError, the structure can't be validated, world: " + world + " , starting pos: " + pos);
            return null;
        }
        BlockPos min_pos = findMinCorner(outline_set);
        BlockPos max_pos = findMaxCorner(outline_set);
        if (getMinSize(min_pos, max_pos) < 1) {
            error_list.add(new TranslationTextComponent("message.colossal_battery.error.too_small").append(material_name));
            return null;
        }
        if (getMaxSize(min_pos, max_pos) >= GeneralConfig.max_size) {
            error_list.add(new TranslationTextComponent("message.colossal_battery.error.too_big", getMaxSize(min_pos, max_pos), GeneralConfig.max_size).append(material_name));
            return null;
        }
        int must_single_block_count = 0;
        for (int x = min_pos.getX(); x <= max_pos.getX(); x++) {
            for (int y = min_pos.getY(); y <= max_pos.getY(); y++) {
                for (int z = min_pos.getZ(); z <= max_pos.getZ(); z++) {
                    BlockPos check_pos = new BlockPos(x, y, z);
                    Block block = world.getBlockState(check_pos).getBlock();
                    if (isFrame(min_pos, max_pos, check_pos)) {
                        if (!validator_frame.test(block)) {
                            error_list.add(new TranslationTextComponent("message.colossal_battery.error.frame_block_invalid", check_pos.toString()).append(material_name));
                        }
                    } else if (isOutline(min_pos, max_pos, check_pos)) {
                        if (!validator_outline.test(block)) {
                            error_list.add(new TranslationTextComponent("message.colossal_battery.error.outline_block_invalid", check_pos.toString()).append(material_name));
                            return null;
                        }
                    } else {
                        if (!validator_inner.test(block)) {
                            error_list.add(new TranslationTextComponent("message.colossal_battery.error.inner_block_invalid", check_pos.toString()).append(material_name));
                            return null;
                        }
                    }
                    if (validator_must_single.test(block)) {
                        if (must_single_block_count >= 1) {
                            error_list.add(new TranslationTextComponent("message.colossal_battery.error.multiple_core_block", check_pos.toString()).append(material_name));
                            return null;
                        }
                        must_single_block_count ++;
                    }
                }
            }
        }
        return new CubeStructure(world, min_pos, max_pos);
    }

    /**
     * Send all of the error messages in the error_list to player.
     * @param player The player to send to.
     */
    public static void sendAllErrorMessage(PlayerEntity player) {
        for (IFormattableTextComponent textComponent : error_list) {
            player.sendStatusMessage(textComponent, false);
        }
    }

    public static boolean isClingWithAnotherStructure(BlockPos min_pos, BlockPos max_pos, World world, TranslationTextComponent material_name) {
        for (int x = min_pos.getX(); x <= max_pos.getX(); x++) {
            for (int y = min_pos.getY(); y <= max_pos.getY(); y++) {
                for (int z = min_pos.getZ(); z <= max_pos.getZ(); z++) {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    if (blockPos.getX() == min_pos.getX()) {
                        if (world.getTileEntity(blockPos.west()) instanceof TileMultiBlockPartBase) {
                            error_list.add(new TranslationTextComponent("message.colossal_battery.error.close_to_another_structure", blockPos.west()).append(material_name));
                            return true;
                        }
                    }
                    if (blockPos.getX() == max_pos.getX()) {
                        if (world.getTileEntity(blockPos.east()) instanceof TileMultiBlockPartBase) {
                            error_list.add(new TranslationTextComponent("message.colossal_battery.error.close_to_another_structure", blockPos.east()).append(material_name));
                            return true;
                        }
                    }
                    if (blockPos.getY() == min_pos.getY()) {
                        if (world.getTileEntity(blockPos.down()) instanceof TileMultiBlockPartBase) {
                            error_list.add(new TranslationTextComponent("message.colossal_battery.error.close_to_another_structure", blockPos.down()).append(material_name));
                            return true;
                        }
                    }
                    if (blockPos.getY() == max_pos.getY()) {
                        if (world.getTileEntity(blockPos.up()) instanceof TileMultiBlockPartBase) {
                            error_list.add(new TranslationTextComponent("message.colossal_battery.error.close_to_another_structure", blockPos.up()).append(material_name));
                            return true;
                        }
                    }
                    if (blockPos.getZ() == min_pos.getZ()) {
                        if (world.getTileEntity(blockPos.north()) instanceof TileMultiBlockPartBase) {
                            error_list.add(new TranslationTextComponent("message.colossal_battery.error.close_to_another_structure", blockPos.north()).append(material_name));
                            return true;
                        }
                    }
                    if (blockPos.getZ() == max_pos.getZ()) {
                        if (world.getTileEntity(blockPos.south()) instanceof TileMultiBlockPartBase) {
                            error_list.add(new TranslationTextComponent("message.colossal_battery.error.close_to_another_structure", blockPos.south()).append(material_name));
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Try find a battery structure from a starting pos.
     * @param world World in.
     * @param pos Starting pos.
     * @param player The player that tries to form this structure (To send error message to), can be null.
     * @return The BatteryStructure object, or null if didn't found anyone.
     */
    public static BatteryStructure validateBatteryStructure(World world, BlockPos pos, @Nullable PlayerEntity player) {
        error_list.clear();
        for (BatteryMaterial material : BatteryMaterials.VALUES) {
            TranslationTextComponent material_name = new TranslationTextComponent("material." + material.name);
            CubeStructure result = validateBoxStructureWithCore(
                    world,
                    pos,
                    material.outline_validator
                            .or(material.core_validator)
                            .or(material.interface_validator),
                    material.frame_validator,
                    material.inner_validator,
                    material.core_validator,
                    material_name
            );
            if (result != null) {
                if (isClingWithAnotherStructure(result.min_pos, result.max_pos, world, material_name)) {
                    sendAllErrorMessage(player);
                    return null;
                }
                return new BatteryStructure(result, material);
            }
        }
        return null;
    }
}
