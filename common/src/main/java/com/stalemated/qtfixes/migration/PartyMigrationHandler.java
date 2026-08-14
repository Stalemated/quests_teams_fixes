package com.stalemated.qtfixes.migration;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.stalemated.qtfixes.QuestsAndTeamsFixes;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.data.PartyTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;

public class PartyMigrationHandler {

    public static void onServerStarted(MinecraftServer server) {
        Collection<Team> teams = FTBTeamsAPI.api().getManager().getTeams();
        if (teams.isEmpty()) {
            return;
        }

        boolean dataChanged = false;
        ServerCommandSource sourceStack = server.getCommandSource();
        
        for (Team team : teams) {
            if (team.isPartyTeam() && team instanceof PartyTeam party) {
                if (party.getMembers().isEmpty()) {
                    disbandParty(party, sourceStack);
                    continue;
                }

                if (QuestMigrator.migratePartyProgressToMembers(party)) {
                    dataChanged = true;
                }
                disbandParty(party, sourceStack);
            }
        }
        
        QuestMigrator.saveIfChanged(dataChanged);
    }

    private static void disbandParty(PartyTeam party, ServerCommandSource sourceStack) {
        try {
            party.forceDisband(sourceStack);
        } catch (CommandSyntaxException e) {
            QuestsAndTeamsFixes.LOGGER.error("Failed to forcefully disband party team: {}", party.getShortName(), e);
        }
    }
}
