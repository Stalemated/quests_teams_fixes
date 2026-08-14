package com.stalemated.qtfixes;

import com.stalemated.qtfixes.migration.PartyMigrationHandler;
import dev.architectury.event.events.common.LifecycleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QuestsAndTeamsFixes {
    public static final String MOD_ID = "quests_teams_fixes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LifecycleEvent.SERVER_STARTED.register(PartyMigrationHandler::onServerStarted);
    }
}
