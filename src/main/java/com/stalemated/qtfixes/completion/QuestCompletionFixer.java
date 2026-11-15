package com.stalemated.qtfixes.completion;

import dev.ftb.mods.ftbquests.events.QuestProgressEventData;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.TeamData;

import com.stalemated.qtfixes.completion.helper.QuestFixerHelper;

public class QuestCompletionFixer {

    public static void processCompletedQuest(Quest quest, QuestProgressEventData<?> data) {
        TeamData teamData = data.getTeamData();
        if (teamData == null) return;

        for (QuestObject questObject : quest.getDependants()) {
            if (questObject instanceof Quest dependantQuest) {
                QuestFixerHelper.processDependantQuest(dependantQuest, teamData);
            }
        }
    }
}
