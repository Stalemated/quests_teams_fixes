package com.stalemated.qtfixes.migration;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.stalemated.qtfixes.QuestsAndTeamsFixes;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamManager;
import dev.ftb.mods.ftbteams.data.PartyTeam;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;

public final class ExistingParties {

    private ExistingParties() {
    }

    public static void serverStarted(MinecraftServer server) {
        TeamManager teams = FTBTeamsAPI.api().getManager();
        ServerQuestFile questFile = ServerQuestFile.INSTANCE;
        CommandSourceStack source = server.createCommandSourceStack();
        boolean progressCopied = false;

        for (Team team : teams.getTeams()) {
            if (!(team instanceof PartyTeam party)) continue;

            if (questFile != null) progressCopied |= copyProgressToMembers(party, teams, questFile);
            disband(party, source);
        }

        if (progressCopied) questFile.saveNow();
    }

    private static boolean copyProgressToMembers(PartyTeam party, TeamManager teams, ServerQuestFile questFile) {
        PartyMigrationPlan<Team> plan = PartyMigrationPlan.forMembers(party.getMembers(), teams::getPlayerTeamForPlayerID);
        plan.membersWithoutPlayerTeam().forEach(member -> QuestsAndTeamsFixes.LOGGER.warn(
                "Party {} member {} has no player team, so the party's quest progress was not copied to them",
                party.getShortName(), member));
        if (!plan.copiesProgress()) return false;

        TeamData partyData = questFile.getOrCreateTeamData(party);
        for (Team playerTeam : plan.progressReceivers()) {
            TeamData playerData = questFile.getOrCreateTeamData(playerTeam);
            playerData.mergeData(partyData);
            playerData.markDirty();
        }
        return true;
    }

    private static void disband(PartyTeam party, CommandSourceStack source) {
        try {
            party.forceDisband(source);
        } catch (CommandSyntaxException e) {
            QuestsAndTeamsFixes.LOGGER.error("Failed to forcefully disband party team: {}", party.getShortName(), e);
        }
    }
}
