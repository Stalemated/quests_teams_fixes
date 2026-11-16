package com.stalemated.qtfixes.mixin.compat.ftbquests;

//? if <1.21.1 {
import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.qtfixes.completion.StuckFlexibleQuests;
import dev.ftb.mods.ftbquests.events.QuestProgressEventData;
import dev.ftb.mods.ftbquests.quest.Quest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@RequiresMod("ftbquests")
@Mixin(value = Quest.class)
public abstract class QuestMixin {

    @Inject(method = "onCompleted", at = @At("TAIL"))
    private void quests_teams_fixes$completeStuckDependants(QuestProgressEventData<?> data, CallbackInfo ci) {
        StuckFlexibleQuests.questCompleted((Quest) (Object) this, data.getTeamData());
    }
}
//?}
