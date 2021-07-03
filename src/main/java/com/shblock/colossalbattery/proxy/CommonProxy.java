package com.shblock.colossalbattery.proxy;

import com.shblock.colossalbattery.ColossalBattery;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.CommonProxyComponent;

public class CommonProxy extends CommonProxyComponent {
    @Override
    public ModBase getMod() {
        return ColossalBattery._instance;
    }
}
