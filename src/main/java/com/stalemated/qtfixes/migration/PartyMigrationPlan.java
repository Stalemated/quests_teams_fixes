package com.stalemated.qtfixes.migration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

record PartyMigrationPlan<T>(List<T> progressReceivers, List<UUID> membersWithoutPlayerTeam) {

    static <T> PartyMigrationPlan<T> forMembers(Collection<UUID> members, Function<UUID, Optional<T>> playerTeams) {
        List<T> receivers = new ArrayList<>();
        List<UUID> withoutPlayerTeam = new ArrayList<>();
        for (UUID member : members) {
            Optional<T> playerTeam = playerTeams.apply(member);
            if (playerTeam.isPresent()) receivers.add(playerTeam.get());
            else withoutPlayerTeam.add(member);
        }
        return new PartyMigrationPlan<>(List.copyOf(receivers), List.copyOf(withoutPlayerTeam));
    }

    boolean disbands() {
        return membersWithoutPlayerTeam.isEmpty();
    }

    boolean copiesProgress() {
        return disbands() && !progressReceivers.isEmpty();
    }
}
