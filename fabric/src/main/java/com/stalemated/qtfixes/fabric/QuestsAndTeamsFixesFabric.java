package com.stalemated.qtfixes.fabric;

import net.fabricmc.api.ModInitializer;

import com.stalemated.qtfixes.QuestsAndTeamsFixes;

public final class QuestsAndTeamsFixesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        QuestsAndTeamsFixes.init();
    }
}
