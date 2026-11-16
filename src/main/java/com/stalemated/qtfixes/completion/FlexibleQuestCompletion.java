package com.stalemated.qtfixes.completion;

//? if <1.21.1 {
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

final class FlexibleQuestCompletion<Q> {
    private final Deque<Check<Q>> checks = new ArrayDeque<>();
    private boolean checking;

    void questCompleted(Q quest, TeamProgress<Q> team) {
        team.dependants(quest).forEach(dependant -> checks.add(new Check<>(dependant, team)));
        runChecks();
    }

    void completeStuckQuests(Collection<Q> quests, TeamProgress<Q> team) {
        quests.forEach(quest -> checks.add(new Check<>(quest, team)));
        runChecks();
    }

    private void runChecks() {
        if (checking) return;

        checking = true;
        try {
            Check<Q> check;
            while ((check = checks.poll()) != null) check.completeIfStuck();
        } finally {
            checks.clear();
            checking = false;
        }
    }

    private record Check<T>(T quest, TeamProgress<T> team) {
        void completeIfStuck() {
            if (isStuck()) team.complete(quest);
        }

        private boolean isStuck() {
            return !team.isLocked()
                    && team.isFlexible(quest)
                    && team.canStartTasks(quest)
                    && team.areDependenciesComplete(quest)
                    && areRequiredTasksFinished(team.tasks(quest))
                    && !team.isCompleted(quest);
        }

        private static boolean areRequiredTasksFinished(Collection<TaskState> tasks) {
            boolean anyRequiredUnfinished = tasks.stream().anyMatch(task -> !task.finished() && !task.optional());
            boolean anyFinished = tasks.stream().anyMatch(TaskState::finished);
            return !anyRequiredUnfinished && anyFinished;
        }
    }
}
//?}
