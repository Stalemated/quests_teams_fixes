package com.stalemated.qtfixes.migration;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PartyMigrationPlanTest {
    private static final UUID ALICE = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID BOB = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID GHOST = UUID.fromString("00000000-0000-0000-0000-000000000003");

    private static final Map<UUID, String> PLAYER_TEAMS = Map.of(ALICE, "alice_team", BOB, "bob_team");

    private static PartyMigrationPlan<String> planFor(UUID... members) {
        return PartyMigrationPlan.forMembers(List.of(members), member -> Optional.ofNullable(PLAYER_TEAMS.get(member)));
    }

    @Test
    void aPartyWhoseMembersAllHaveAPlayerTeamCopiesItsProgressAndIsDisbanded() {
        PartyMigrationPlan<String> plan = planFor(ALICE, BOB);

        assertEquals(List.of("alice_team", "bob_team"), plan.progressReceivers());
        assertTrue(plan.copiesProgress());
        assertTrue(plan.disbands());
    }

    @Test
    void aPartyWithAMemberWithoutAPlayerTeamIsKeptAndCopiesNothing() {
        PartyMigrationPlan<String> plan = planFor(ALICE, GHOST);

        assertEquals(List.of(GHOST), plan.membersWithoutPlayerTeam());
        assertFalse(plan.copiesProgress());
        assertFalse(plan.disbands());
    }

    @Test
    void anEmptyPartyIsDisbandedWithoutCopyingAnything() {
        PartyMigrationPlan<String> plan = planFor();

        assertFalse(plan.copiesProgress());
        assertTrue(plan.disbands());
    }
}
