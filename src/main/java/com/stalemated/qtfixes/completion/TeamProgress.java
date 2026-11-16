package com.stalemated.qtfixes.completion;

import java.util.Collection;

interface TeamProgress<Q> {
    Collection<Q> dependants(Q quest);

    boolean isLocked();

    boolean isCompleted(Q quest);

    boolean isFlexible(Q quest);

    boolean canStartTasks(Q quest);

    boolean areDependenciesComplete(Q quest);

    Collection<TaskState> tasks(Q quest);

    void complete(Q quest);
}
