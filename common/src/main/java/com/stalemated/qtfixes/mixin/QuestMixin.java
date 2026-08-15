package com.stalemated.qtfixes.mixin;

import com.stalemated.qtfixes.completion.QuestCompletionFixer;
import dev.ftb.mods.ftbquests.events.QuestProgressEventData;
import dev.ftb.mods.ftbquests.quest.Quest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Quest.class)
public abstract class QuestMixin {

    @Inject(method = "onCompleted", at = @At("TAIL"))
    private void forceDependantCompletion(QuestProgressEventData<?> data, CallbackInfo ci) {
        Quest quest = (Quest) (Object) this;
        QuestCompletionFixer.processCompletedQuest(quest, data);
    }
}
