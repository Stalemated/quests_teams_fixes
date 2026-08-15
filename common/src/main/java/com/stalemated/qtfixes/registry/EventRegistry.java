package com.stalemated.qtfixes.registry;

import com.stalemated.qtfixes.completion.RetroactiveQuestFixer;
import com.stalemated.qtfixes.migration.PartyMigrationHandler;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;

public class EventRegistry {
    public static void register() {
        LifecycleEvent.SERVER_STARTED.register(PartyMigrationHandler::onServerStarted);
        PlayerEvent.PLAYER_JOIN.register(RetroactiveQuestFixer::onPlayerJoin);
    }
}
