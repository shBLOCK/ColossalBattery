package com.shblock.colossalbattery.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ClientWorldGetter {
    public static World readWorldClient() {
        return Minecraft.getInstance().world; //TODO:find better solution to get world on client
    }
}
