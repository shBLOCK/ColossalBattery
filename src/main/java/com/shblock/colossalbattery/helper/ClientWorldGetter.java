package com.shblock.colossalbattery.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public class ClientWorldGetter {
    public static World readWorldClient(RegistryKey<World> worldRegistryKey) {
        if (Minecraft.getInstance().isIntegratedServerRunning()) {
            return Minecraft.getInstance().getIntegratedServer().getWorld(worldRegistryKey);
        }
        return Minecraft.getInstance().world; //TODO:find better solution to get world on client
    }
}
