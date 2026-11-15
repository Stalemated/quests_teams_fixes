package com.stalemated.qtfixes.completion;

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
            return team.isFlexible(quest)
                    && team.areDependenciesComplete(quest)
                    && team.areTasksFinished(quest)
                    && !team.isCompleted(quest);
        }
    }
}
