package com.stalemated.qtfixes.completion;

import com.stalemated.qtfixes.config.ActiveFixes;
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
        if (team == null || !ActiveFixes.current().fixFlexibleQuests()) return;

        COMPLETION.questCompleted(quest, new FtbTeamProgress(team));
    }

    public static void playerJoined(ServerPlayer player) {
        if (!ActiveFixes.current().fixFlexibleQuests()) return;

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
