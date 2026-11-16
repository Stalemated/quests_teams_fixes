package com.stalemated.qtfixes.party;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.stalemated.qtfixes.config.ActiveFixes;
import net.minecraft.network.chat.Component;

public final class PartyCreation {
    private static final SimpleCommandExceptionType BLOCKED = new SimpleCommandExceptionType(
            Component.translatableWithFallback("quests_teams_fixes.message.parties_disabled", "Parties are disabled on this server"));

    private PartyCreation() {
    }

    public static void checkAllowed() throws CommandSyntaxException {
        if (ActiveFixes.current().blockPartyCreation()) throw BLOCKED.create();
    }
}
