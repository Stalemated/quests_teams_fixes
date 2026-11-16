package com.stalemated.qtfixes.config;

import com.stalemated.lib.config.annotation.Comment;
import com.stalemated.lib.config.annotation.Nest;

public class QuestsAndTeamsFixesConfig {
    @Comment("""
            Used by the server that runs this file: a dedicated server, or this game in singleplayer and when hosting a LAN world.
            On a client connected to another server it does nothing.""")
    @Nest
    public Server server = new Server();

    @Comment("Used by the game client. A dedicated server ignores it.")
    @Nest
    public Client client = new Client();

    public static class Server {
        @Comment("""
                Blocks party creation from the FTB Teams screen, from commands and from other mods.
                Turning this off lets players share quest progress. Some modpack quests run special code when completed and can break for a whole party.""")
        public boolean blockPartyCreation = true;

        @Comment("""
                On server start, copies each party's quest progress to its members and disbands the party.
                Only runs while blockPartyCreation is true.""")
        public boolean disbandPartiesOnStart = true;

        @Comment("Removes the /ftbteams command. Needs a server restart.")
        public boolean removeFtbTeamsCommand = true;

        //? if <1.21.1 {
        @Comment("""
                Completes FLEXIBLE quests whose tasks were finished before their dependencies, also when a player joins.
                FTB Quests 2001.4.22+ already has this fix bundled-in.""")
        public boolean fixFlexibleQuests = true;
        //?}
    }

    public static class Client {
        @Comment("Hides every FTB Library sidebar button, including the FTB Quests book. Needs a game restart or F3+T.")
        public boolean hideSidebarButtons = true;
    }
}
