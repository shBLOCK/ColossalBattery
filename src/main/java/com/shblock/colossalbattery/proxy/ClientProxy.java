package com.shblock.colossalbattery.proxy;

import com.shblock.colossalbattery.ColossalBattery;
import com.shblock.colossalbattery.client.render.battery.MekRender;
import com.shblock.colossalbattery.client.render.battery.RenderBattery6Face;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;

public class ClientProxy extends ClientProxyComponent {
    public ClientProxy() {
        super(new CommonProxy());
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(RenderBattery6Face::onPreTextureStitch);
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(MekRender::onPreTextureStitch);
    }

    @Override
    public ModBase getMod() {
        return ColossalBattery._instance;
    }
}
