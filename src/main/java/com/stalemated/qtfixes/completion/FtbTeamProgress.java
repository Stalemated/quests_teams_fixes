package com.stalemated.qtfixes.completion;

import dev.ftb.mods.ftbquests.events.QuestProgressEventData;
import dev.ftb.mods.ftbquests.quest.ProgressionMode;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;

import java.util.Collection;
import java.util.Date;
import java.util.List;

record FtbTeamProgress(TeamData team) implements TeamProgress<Quest> {

    @Override
    public Collection<Quest> dependants(Quest quest) {
        return quest.getDependants().stream()
                .filter(Quest.class::isInstance)
                .map(Quest.class::cast)
                .toList();
    }

    @Override
    public boolean isCompleted(Quest quest) {
        return team.isCompleted(quest);
    }

    @Override
    public boolean isFlexible(Quest quest) {
        return quest.getProgressionMode() == ProgressionMode.FLEXIBLE;
    }

    @Override
    public boolean areDependenciesComplete(Quest quest) {
        return quest.areDependenciesComplete(team);
    }

    @Override
    public boolean areTasksFinished(Quest quest) {
        Collection<Task> tasks = quest.getTasks();
        return !tasks.isEmpty() && tasks.stream().allMatch(task -> team.getProgress(task.id) >= task.getMaxProgress());
    }

    @Override
    public void complete(Quest quest) {
        for (Task task : quest.getTasks()) {
            if (!team.isCompleted(task)) team.markTaskCompleted(task);
        }

        // If the quest STILL didn't complete after markTaskCompleted(), force it
        if (!team.isCompleted(quest)) {
            quest.onCompleted(new QuestProgressEventData<>(new Date(), team, quest, team.getOnlineMembers(), List.of()));
        }
    }
}
