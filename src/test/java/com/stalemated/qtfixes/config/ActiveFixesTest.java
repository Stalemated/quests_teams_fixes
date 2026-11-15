package com.stalemated.qtfixes.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActiveFixesTest {

    @Test
    void theDefaultConfigKeepsEveryFixOn() {
        ActiveFixes fixes = new ActiveFixes(new QuestsAndTeamsFixesConfig());

        assertTrue(fixes.blockPartyCreation());
        assertTrue(fixes.disbandPartiesOnStart());
        assertTrue(fixes.removeFtbTeamsCommand());
        assertTrue(fixes.fixFlexibleQuests());
        assertTrue(fixes.hideSidebarButtons());
    }

    @Test
    void turningEveryOptionOffTurnsEveryFixOff() {
        QuestsAndTeamsFixesConfig config = new QuestsAndTeamsFixesConfig();
        config.server.blockPartyCreation = false;
        config.server.disbandPartiesOnStart = false;
        config.server.removeFtbTeamsCommand = false;
        config.server.fixFlexibleQuests = false;
        config.client.hideSidebarButtons = false;

        ActiveFixes fixes = new ActiveFixes(config);

        assertFalse(fixes.blockPartyCreation());
        assertFalse(fixes.disbandPartiesOnStart());
        assertFalse(fixes.removeFtbTeamsCommand());
        assertFalse(fixes.fixFlexibleQuests());
        assertFalse(fixes.hideSidebarButtons());
    }

    @Test
    void partiesAreNotDisbandedWhilePartyCreationIsAllowed() {
        QuestsAndTeamsFixesConfig config = new QuestsAndTeamsFixesConfig();
        config.server.blockPartyCreation = false;

        ActiveFixes fixes = new ActiveFixes(config);

        assertFalse(fixes.blockPartyCreation());
        assertFalse(fixes.disbandPartiesOnStart());
    }
}
