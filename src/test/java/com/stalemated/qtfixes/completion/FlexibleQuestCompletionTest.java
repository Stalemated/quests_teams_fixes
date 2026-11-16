package com.stalemated.qtfixes.completion;

//? if <1.21.1 {
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
    private static final TaskState REQUIRED_FINISHED = new TaskState(true, false);
    private static final TaskState REQUIRED_UNFINISHED = new TaskState(false, false);
    private static final TaskState OPTIONAL_FINISHED = new TaskState(true, true);
    private static final TaskState OPTIONAL_UNFINISHED = new TaskState(false, true);

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

    @Test
    void aLockedTeamGetsNothingCompleted() {
        QuestBook book = new QuestBook()
                .quest("root")
                .stuckQuest("first", "root");
        book.locked = true;

        book.completeByPlayer("root");

        assertEquals(List.of("root"), book.completions);
    }

    @Test
    void aQuestThatCannotStartItsTasksIsNotCompleted() {
        QuestBook book = new QuestBook()
                .quest("root")
                .stuckQuest("excluded_or_waiting_to_repeat", "root");
        book.cannotStartTasks.add("excluded_or_waiting_to_repeat");

        book.completeByPlayer("root");

        assertEquals(List.of("root"), book.completions);
    }

    @Test
    void anUnfinishedOptionalTaskDoesNotHoldTheQuestBack() {
        QuestBook book = new QuestBook()
                .quest("root")
                .questWithTasks("with_optional", List.of(REQUIRED_FINISHED, OPTIONAL_UNFINISHED), "root");

        book.completeByPlayer("root");

        assertEquals(List.of("root", "with_optional"), book.completions);
    }

    @Test
    void aQuestWithOnlyOptionalTasksNeedsOneOfThemFinished() {
        QuestBook book = new QuestBook()
                .quest("root")
                .questWithTasks("none_finished", List.of(OPTIONAL_UNFINISHED, OPTIONAL_UNFINISHED), "root")
                .questWithTasks("one_finished", List.of(OPTIONAL_UNFINISHED, OPTIONAL_FINISHED), "root");

        book.completeByPlayer("root");

        assertEquals(List.of("root", "one_finished"), book.completions);
    }

    private static final class QuestBook implements TeamProgress<String> {
        private final FlexibleQuestCompletion<String> completion = new FlexibleQuestCompletion<>();
        private final Map<String, List<String>> dependencies = new LinkedHashMap<>();
        private final Set<String> flexible = new HashSet<>();
        private final Map<String, List<TaskState>> tasks = new LinkedHashMap<>();
        private final Set<String> cannotStartTasks = new HashSet<>();
        private final Set<String> completed = new HashSet<>();
        private final List<String> completions = new ArrayList<>();
        private boolean locked;
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
            tasks.put(quest, List.of(tasksFinished ? REQUIRED_FINISHED : REQUIRED_UNFINISHED));
            return this;
        }

        QuestBook questWithTasks(String quest, List<TaskState> questTasks, String... dependsOn) {
            quest(quest, true, false, dependsOn);
            tasks.put(quest, questTasks);
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
        public boolean isLocked() {
            return locked;
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
        public boolean canStartTasks(String quest) {
            return !cannotStartTasks.contains(quest);
        }

        @Override
        public boolean areDependenciesComplete(String quest) {
            return completed.containsAll(dependencies.get(quest));
        }

        @Override
        public Collection<TaskState> tasks(String quest) {
            return tasks.get(quest);
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
//?}
