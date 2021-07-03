package com.shblock.colossalbattery.helper;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.function.Predicate;

public class MultiBlockHelper {
    /**
     * Find valid core blocks that's connected with the starting pos with valid connect blocks or another valid core block.
     * @param world World in.
     * @param pos Starting pos.
     * @param validator_connect The block validator for connect blocks.
     * @param validator_core The block validator for core blocks.
     * @param set Scanned blocks (for recursion).
     * @param core_set Found core blocks (for recursion and result).
     * @return The current founded core blocks.
     */
    private static HashSet<BlockPos> findCore(World world, BlockPos pos, Predicate<Block> validator_connect, Predicate<Block> validator_core, HashSet<BlockPos> set, HashSet<BlockPos> core_set) {
        Block block = world.getBlockState(pos).getBlock();
        if (validator_core.test(block)) {
            core_set.add(pos);
        }
        if (validator_connect.test(block)) {
            set.add(pos);
            for (Direction direction : Direction.values()) {
                BlockPos offset_pos = pos.offset(direction);
                if (!set.contains(offset_pos)) {
                    findCore(world, offset_pos, validator_connect, validator_core, set, core_set);
                }
            }
        }
        return core_set;
    }

    /**
     * For calling the upwards function easily (if no is found or multiple is found, will return null).
     * @param world World in.
     * @param pos Starting pos.
     * @param validator_connect The block validator for connect blocks.
     * @param validator_core The block validator for core blocks.
     * @return The founded core block (if no is found or multiple is found, will return null).
     */
    public static BlockPos findCore(World world, BlockPos pos, Predicate<Block> validator_connect, Predicate<Block> validator_core) {
        HashSet<BlockPos> core_set = findCore(world, pos, validator_connect, validator_core, new HashSet<>(), new HashSet<>());
        return core_set.size() != 1 ? null : core_set.iterator().next();
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
                    scanConnectedBlocks(world, offset_pos, validator, set);
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
     * Find a cube structure with valid outline and inner blocks from a starting pos.
     * @param world World in.
     * @param pos Starting pos.
     * @param validator_outline The block validator for outline block.
     * @param validator_inner The block validator for core block.
     * @return The CubeStructure object, or null if didn't found any structure.
     */
    public static CubeStructure validateBoxWithCore(World world, BlockPos pos, Predicate<Block> validator_outline, Predicate<Block> validator_inner) {
        HashSet<BlockPos> full_set = scanConnectedBlocks(world, pos, validator_outline.or(validator_inner));
        BlockPos min_pos = findMinCorner(full_set);
        BlockPos max_pos = findMaxCorner(full_set);
        for (int x = min_pos.getX(); x <= max_pos.getX(); x++) {
            for (int y = min_pos.getY(); y <= max_pos.getY(); y++) {
                for (int z = min_pos.getZ(); z <= max_pos.getZ(); z++) {
                    BlockPos check_pos = new BlockPos(x, y, z);
                    if (isOutline(min_pos, max_pos, check_pos)) {
                        if (!validator_outline.test(world.getBlockState(check_pos).getBlock())) {
                            return null;
                        }
                    } else {
                        if (!validator_inner.test(world.getBlockState(check_pos).getBlock())) {
                            return null;
                        }
                    }
                }
            }
        }
        return new CubeStructure(min_pos, max_pos);
    }
}
