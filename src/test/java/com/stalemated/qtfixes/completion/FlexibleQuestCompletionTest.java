package com.stalemated.qtfixes.completion;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlexibleQuestCompletionTest {

    @Test
    void aChainOfFinishedFlexibleQuestsCompletesWhenItsFirstDependencyDoes() {
        QuestBook book = new QuestBook()
                .quest("root")
                .stuckQuest("first", "root")
                .stuckQuest("second", "first")
                .stuckQuest("third", "second");

        book.completeByPlayer("root");

        assertEquals(List.of("root", "first", "second", "third"), book.completions);
    }

    @Test
    void aQuestWithAnUnfinishedDependencyStaysIncomplete() {
        QuestBook book = new QuestBook()
                .quest("root")
                .quest("other")
                .stuckQuest("waiting", "root", "other");

        book.completeByPlayer("root");

        assertEquals(List.of("root"), book.completions);
    }

    @Test
    void aQuestWaitingForTwoDependenciesCompletesWithTheSecondOne() {
        QuestBook book = new QuestBook()
                .quest("root")
                .quest("other")
                .stuckQuest("waiting", "root", "other");

        book.completeByPlayer("root");
        book.completeByPlayer("other");

        assertEquals(List.of("root", "other", "waiting"), book.completions);
    }

    @Test
    void aQuestWithUnfinishedTasksOrOutsideFlexibleModeIsNotTouched() {
        QuestBook book = new QuestBook()
                .quest("root")
                .quest("unfinished", true, false, "root")
                .quest("default_mode", false, true, "root");

        book.completeByPlayer("root");

        assertEquals(List.of("root"), book.completions);
    }

    @Test
    void anAlreadyCompletedQuestIsNotCompletedAgain() {
        QuestBook book = new QuestBook()
                .quest("root")
                .stuckQuest("first", "root");

        book.completeByPlayer("root");
        book.completeByPlayer("root");

        assertEquals(List.of("root", "first", "root"), book.completions);
    }

    @Test
    void completionsDuringTheCheckAreQueuedInsteadOfNested() {
        QuestBook book = new QuestBook()
                .quest("root")
                .stuckQuest("first", "root")
                .stuckQuest("second", "first")
                .stuckQuest("third", "second");

        book.completeByPlayer("root");

        assertEquals(2, book.deepestCompletion);
    }

    @Test
    void stuckQuestsAreCompletedOnJoinWhateverTheirOrder() {
        QuestBook book = new QuestBook()
                .quest("root")
                .stuckQuest("second", "first")
                .stuckQuest("first", "root");
        book.completed.add("root");

        book.completion.completeStuckQuests(book.quests(), book);

        assertEquals(List.of("first", "second"), book.completions);
    }

    private static final class QuestBook implements TeamProgress<String> {
        private final FlexibleQuestCompletion<String> completion = new FlexibleQuestCompletion<>();
        private final Map<String, List<String>> dependencies = new LinkedHashMap<>();
        private final Set<String> flexible = new HashSet<>();
        private final Set<String> finishedTasks = new HashSet<>();
        private final Set<String> completed = new HashSet<>();
        private final List<String> completions = new ArrayList<>();
        private int depth;
        private int deepestCompletion;

        QuestBook quest(String quest) {
            return quest(quest, true, false);
        }

        QuestBook stuckQuest(String quest, String... dependsOn) {
            return quest(quest, true, true, dependsOn);
        }

        QuestBook quest(String quest, boolean isFlexible, boolean tasksFinished, String... dependsOn) {
            dependencies.put(quest, List.of(dependsOn));
            if (isFlexible) flexible.add(quest);
            if (tasksFinished) finishedTasks.add(quest);
            return this;
        }

        Collection<String> quests() {
            return dependencies.keySet();
        }

        void completeByPlayer(String quest) {
            complete(quest);
        }

        @Override
        public Collection<String> dependants(String quest) {
            return dependencies.entrySet().stream()
                    .filter(entry -> entry.getValue().contains(quest))
                    .map(Map.Entry::getKey)
                    .toList();
        }

        @Override
        public boolean isCompleted(String quest) {
            return completed.contains(quest);
        }

        @Override
        public boolean isFlexible(String quest) {
            return flexible.contains(quest);
        }

        @Override
        public boolean areDependenciesComplete(String quest) {
            return completed.containsAll(dependencies.get(quest));
        }

        @Override
        public boolean areTasksFinished(String quest) {
            return finishedTasks.contains(quest);
        }

        @Override
        public void complete(String quest) {
            depth++;
            deepestCompletion = Math.max(deepestCompletion, depth);
            completed.add(quest);
            completions.add(quest);
            completion.questCompleted(quest, this);
            depth--;
        }
    }
}
