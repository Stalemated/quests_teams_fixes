package com.stalemated.qtfixes.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.stalemated.qtfixes.QuestsAndTeamsFixes;

@Mod(QuestsAndTeamsFixes.MOD_ID)
@SuppressWarnings("removal")
public final class QuestsAndTeamsFixesForge {
    public QuestsAndTeamsFixesForge() {
        EventBuses.registerModEventBus(QuestsAndTeamsFixes.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        QuestsAndTeamsFixes.init();
    }
}
