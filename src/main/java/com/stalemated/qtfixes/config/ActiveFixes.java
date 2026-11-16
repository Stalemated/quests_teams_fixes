package com.stalemated.qtfixes.config;

public record ActiveFixes(QuestsAndTeamsFixesConfig config) {

    public static ActiveFixes current() {
        return new ActiveFixes(QuestsAndTeamsFixesConfigRegistry.CONFIG.get());
    }

    public boolean blockPartyCreation() {
        return config.server.blockPartyCreation;
    }

    public boolean disbandPartiesOnStart() {
        return config.server.blockPartyCreation && config.server.disbandPartiesOnStart;
    }

    public boolean removeFtbTeamsCommand() {
        return config.server.removeFtbTeamsCommand;
    }

    //? if <1.21.1 {
    public boolean fixFlexibleQuests() {
        return config.server.fixFlexibleQuests;
    }
    //?}

    public boolean hideSidebarButtons() {
        return config.client.hideSidebarButtons;
    }
}
