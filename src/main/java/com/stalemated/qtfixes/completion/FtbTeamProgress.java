package com.stalemated.qtfixes.completion;

//? if <1.21.1 {
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
    public boolean isLocked() {
        return team.isLocked();
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
    public boolean canStartTasks(Quest quest) {
        return team.canStartTasks(quest);
    }

    @Override
    public boolean areDependenciesComplete(Quest quest) {
        return quest.areDependenciesComplete(team);
    }

    @Override
    public Collection<TaskState> tasks(Quest quest) {
        return quest.getTasks().stream()
                .map(task -> new TaskState(team.isCompleted(task) || isFull(task), task.isOptionalForProgression(team)))
                .toList();
    }

    @Override
    public void complete(Quest quest) {
        for (Task task : quest.getTasks()) {
            if (!team.isCompleted(task) && isFull(task)) team.markTaskCompleted(task);
        }

        // If the quest STILL didn't complete after markTaskCompleted(), force it, but only when FTB counts its tasks as complete
        if (!team.isCompleted(quest) && quest.isCompletedRaw(team)) {
            quest.onCompleted(new QuestProgressEventData<>(new Date(), team, quest, team.getOnlineMembers(), List.of()));
        }
    }

    private boolean isFull(Task task) {
        return team.getProgress(task.id) >= task.getMaxProgress();
    }
}
//?}
