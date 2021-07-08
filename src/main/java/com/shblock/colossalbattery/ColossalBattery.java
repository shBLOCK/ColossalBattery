package com.shblock.colossalbattery;

import com.shblock.colossalbattery.proxy.ClientProxy;
import com.shblock.colossalbattery.proxy.CommonProxy;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.init.ItemGroupMod;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;

@Mod(ColossalBattery.MODID)
public class ColossalBattery extends ModBaseVersionable<ColossalBattery> {

    public static final String MODID = "colossal_battery";

    public static ColossalBattery _instance;

    public ColossalBattery() {
        super(MODID, (instance) -> _instance = instance);
    }

    @Override
    public ItemGroup constructDefaultItemGroup() {
        return new ItemGroupMod(this, () -> RegistryEntries.ITEM_BATTERY_CORE);
    }

    @Override
    protected IClientProxy constructClientProxy() {
        return new ClientProxy();
    }

    @Override
    protected ICommonProxy constructCommonProxy() {
        return new CommonProxy();
    }

    @Override
    protected void onConfigsRegister(ConfigHandler configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig());

        Configs.RegisterConfig(configHandler);
    }

    public static void clog(String message) {
        ColossalBattery._instance.log(Level.INFO, message);
    }

    public static void clog(Level level, String message) {
        ColossalBattery._instance.log(level, message);
    }
}
