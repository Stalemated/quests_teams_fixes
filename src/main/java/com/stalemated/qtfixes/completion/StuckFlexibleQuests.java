package com.stalemated.qtfixes.completion;

import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public final class StuckFlexibleQuests {
    private static final FlexibleQuestCompletion<Quest> COMPLETION = new FlexibleQuestCompletion<>();

    private StuckFlexibleQuests() {
    }

    public static void questCompleted(Quest quest, TeamData team) {
        if (team == null) return;

        COMPLETION.questCompleted(quest, new FtbTeamProgress(team));
    }

    public static void playerJoined(ServerPlayer player) {
        ServerQuestFile questFile = ServerQuestFile.INSTANCE;
        if (questFile == null) return;

        TeamData team = questFile.getOrCreateTeamData(player);
        if (team == null) return;

        List<Quest> quests = questFile.getAllObjects().stream()
                .filter(Quest.class::isInstance)
                .map(Quest.class::cast)
                .toList();
        COMPLETION.completeStuckQuests(quests, new FtbTeamProgress(team));
    }
}
