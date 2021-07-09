package com.shblock.colossalbattery.helper;

import net.minecraft.nbt.StringNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class NBTHelper {
    public static StringNBT writeWorld(World world) {
        if (world != null) {
            return StringNBT.valueOf(world.getDimensionKey().getLocation().toString());
        }
        return StringNBT.valueOf("");
    }

    public static World readWorld(String string) {
        RegistryKey<World> worldRegistryKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(string));
        return DistExecutor.unsafeRunForDist(
                () -> () -> ClientWorldGetter.readWorldClient(worldRegistryKey),
                () -> () -> readWorldServer(worldRegistryKey)
        );
    }



    public static World readWorldServer(RegistryKey<World> worldRegistryKey) {
        return ServerLifecycleHooks.getCurrentServer().getWorld(worldRegistryKey);
    }

    public static World readWorld(StringNBT tag) {
        return readWorld(tag.getString());
    }
}
