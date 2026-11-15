package com.stalemated.qtfixes.migration;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.stalemated.qtfixes.QuestsAndTeamsFixes;
import com.stalemated.qtfixes.config.ActiveFixes;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamManager;
import dev.ftb.mods.ftbteams.data.PartyTeam;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public final class ExistingParties {

    private ExistingParties() {
    }

    public static void serverStarted(MinecraftServer server) {
        if (!ActiveFixes.current().disbandPartiesOnStart()) return;

        ServerQuestFile questFile = ServerQuestFile.INSTANCE;
        if (questFile == null || questFile.getFolder() == null) {
            QuestsAndTeamsFixes.LOGGER.warn("FTB Quests has not loaded its data yet, so existing parties were not migrated or disbanded");
            return;
        }

        TeamManager teams = FTBTeamsAPI.api().getManager();
        CommandSourceStack source = server.createCommandSourceStack();
        boolean progressCopied = false;

        for (Team team : teams.getTeams()) {
            if (!(team instanceof PartyTeam party)) continue;

            PartyMigrationPlan<Team> plan = PartyMigrationPlan.forMembers(party.getMembers(), teams::getPlayerTeamForPlayerID);
            if (!plan.disbands()) {
                QuestsAndTeamsFixes.LOGGER.warn(
                        "Kept party {} because members {} have no player team and would lose the party's quest progress",
                        party.getShortName(), plan.membersWithoutPlayerTeam());
                continue;
            }

            if (plan.copiesProgress()) {
                copyProgress(party, plan.progressReceivers(), questFile);
                progressCopied = true;
            }
            disband(party, source);
        }

        if (progressCopied) questFile.saveNow();
    }

    private static void copyProgress(PartyTeam party, List<Team> playerTeams, ServerQuestFile questFile) {
        TeamData partyData = questFile.getOrCreateTeamData(party);
        for (Team playerTeam : playerTeams) {
            TeamData playerData = questFile.getOrCreateTeamData(playerTeam);
            playerData.mergeData(partyData);
            playerData.markDirty();
        }
    }

    private static void disband(PartyTeam party, CommandSourceStack source) {
        try {
            party.forceDisband(source);
        } catch (CommandSyntaxException e) {
            QuestsAndTeamsFixes.LOGGER.error("Failed to forcefully disband party team: {}", party.getShortName(), e);
        }
    }
}
