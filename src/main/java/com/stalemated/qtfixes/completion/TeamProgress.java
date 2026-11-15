package com.stalemated.qtfixes.completion;

import java.util.Collection;

interface TeamProgress<Q> {
    Collection<Q> dependants(Q quest);

    boolean isCompleted(Q quest);

    boolean isFlexible(Q quest);

    boolean areDependenciesComplete(Q quest);

    boolean areTasksFinished(Q quest);

    void complete(Q quest);
}
