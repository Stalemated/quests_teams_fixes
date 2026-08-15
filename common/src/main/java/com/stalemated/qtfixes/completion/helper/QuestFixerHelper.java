package com.stalemated.qtfixes.completion.helper;

import dev.ftb.mods.ftbquests.events.QuestProgressEventData;
import dev.ftb.mods.ftbquests.quest.ProgressionMode;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class QuestFixerHelper {
    public static boolean isEligibleForForceCompletion(Quest dependantQuest, TeamData teamData) {
        return dependantQuest.getProgressionMode() == ProgressionMode.FLEXIBLE && dependantQuest.areDependenciesComplete(teamData);
    }

    public static void processDependantQuest(Quest dependantQuest, TeamData teamData) {
        if (!isEligibleForForceCompletion(dependantQuest, teamData)) return;

        Collection<Task> taskList = dependantQuest.getTasks();
        if (areAllTasksCompleted(teamData, taskList) && !teamData.isCompleted(dependantQuest)) {
            forceCompleteTasksAndQuest(dependantQuest, teamData, taskList);
        }
    }

    private static boolean areAllTasksCompleted(TeamData teamData, Collection<Task> taskList) {
        if (taskList.isEmpty()) return false;

        for (Task task : taskList) {
            if (teamData.getProgress(task.id) < task.getMaxProgress()) {
                return false;
            }
        }
        return true;
    }

    private static void forceCompleteTasksAndQuest(Quest dependantQuest, TeamData teamData, Collection<Task> taskList) {
        for (Task task : taskList) {
            if (!teamData.isCompleted(task)) {
                teamData.markTaskCompleted(task);
            }
        }

        // If the quest STILL didn't complete after markTaskCompleted(), force it
        if (!teamData.isCompleted(dependantQuest)) {
            dependantQuest.onCompleted(new QuestProgressEventData<>(
                    new Date(), teamData, dependantQuest, teamData.getOnlineMembers(), List.of()
            ));
        }
    }
}
