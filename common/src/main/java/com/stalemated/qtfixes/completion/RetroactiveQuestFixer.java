package com.stalemated.qtfixes.completion;

import com.stalemated.qtfixes.completion.helper.QuestFixerHelper;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import net.minecraft.server.network.ServerPlayerEntity;

public class RetroactiveQuestFixer {

    public static void onPlayerJoin(ServerPlayerEntity player) {
        if (ServerQuestFile.INSTANCE == null) return;

        TeamData teamData = ServerQuestFile.INSTANCE.getOrCreateTeamData(player);
        if (teamData == null) return;

        for (QuestObjectBase questObject : ServerQuestFile.INSTANCE.getAllObjects()) {
            if (questObject instanceof Quest dependantQuest) {
                QuestFixerHelper.processDependantQuest(dependantQuest, teamData);
            }
        }
    }
}
