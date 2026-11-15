package com.stalemated.qtfixes.migration;

import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.data.PartyTeam;

import java.util.UUID;

public class QuestMigrator {

    public static boolean migratePartyProgressToMembers(PartyTeam party) {
        if (ServerQuestFile.INSTANCE == null) {
            return false;
        }

        boolean anyChanged = false;
        TeamData partyData = ServerQuestFile.INSTANCE.getOrCreateTeamData(party);
        
        for (UUID memberId : party.getMembers()) {
            if (migrateMemberData(partyData, memberId)) {
                anyChanged = true;
            }
        }
        
        return anyChanged;
    }

    private static boolean migrateMemberData(TeamData partyData, UUID memberId) {
        Team playerTeam = FTBTeamsAPI.api().getManager().getPlayerTeamForPlayerID(memberId).orElse(null);
        
        if (playerTeam == null) {
            return false;
        }

        TeamData playerData = ServerQuestFile.INSTANCE.getOrCreateTeamData(playerTeam);
        playerData.mergeData(partyData);
        playerData.mergeClaimedRewards(partyData);
        playerData.markDirty();
        
        return true;
    }

    public static void saveIfChanged(boolean dataChanged) {
        if (dataChanged && ServerQuestFile.INSTANCE != null) {
            ServerQuestFile.INSTANCE.saveNow();
        }
    }
}
