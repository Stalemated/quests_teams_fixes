package com.stalemated.qtfixes.registry;

import com.stalemated.lib.helper.PlatformHelper;
import com.stalemated.qtfixes.completion.RetroactiveQuestFixer;
import com.stalemated.qtfixes.migration.PartyMigrationHandler;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;

public class EventRegistry {
    public static void register() {
        if (areFtbModsLoaded()) {
            LifecycleEvent.SERVER_STARTED.register(PartyMigrationHandler::onServerStarted);
            PlayerEvent.PLAYER_JOIN.register(RetroactiveQuestFixer::onPlayerJoin);
        }
    }

    private static boolean areFtbModsLoaded() {
        return PlatformHelper.INSTANCE.isModLoaded("ftbquests") &&
                PlatformHelper.INSTANCE.isModLoaded("ftbteams") &&
                PlatformHelper.INSTANCE.isModLoaded("ftblibrary");
    }
}
