package com.shblock.colossalbattery.tileentity;

import com.google.common.collect.Sets;
import com.shblock.colossalbattery.ColossalBattery;
import com.shblock.colossalbattery.RegistryEntries;
import com.shblock.colossalbattery.client.render.tile.RenderTileBatteryCore;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;

public class TileBatteryCoreConfig extends TileEntityConfig<TileBatteryCore> {
    public TileBatteryCoreConfig() {
        super(
                ColossalBattery._instance,
                "battery_core",
                eConfig -> new TileEntityType<>(TileBatteryCore::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_BATTERY_CORE), null)
        );
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        getMod().getProxy().registerRenderer(getInstance(), RenderTileBatteryCore::new);
    }
}
