package com.stalemated.qtfixes;

import com.stalemated.lib.helper.PlatformHelper;
import com.stalemated.lib.mixin.ConditionalMixinPlugin;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class QuestsAndTeamsFixesMixinPlugin implements IMixinConfigPlugin {
    //? if <1.21.1 {
    private static final List<String> FLEXIBLE_QUEST_MIXINS = List.of("compat.ftbquests.QuestMixin");
    //?} else
    //private static final List<String> FLEXIBLE_QUEST_MIXINS = List.of();

    private final ConditionalMixinPlugin requiredMods = new ConditionalMixinPlugin();

    @Override
    public void onLoad(String mixinPackage) {
        requiredMods.onLoad(mixinPackage);
    }

    @Override
    public String getRefMapperConfig() {
        return requiredMods.getRefMapperConfig();
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return requiredMods.shouldApplyMixin(targetClassName, mixinClassName);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        requiredMods.acceptTargets(myTargets, otherTargets);
    }

    @Override
    public List<String> getMixins() {
        return PlatformHelper.isModLoadedAtLaunch("ftbquests") ? FLEXIBLE_QUEST_MIXINS : List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        requiredMods.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        requiredMods.postApply(targetClassName, targetClass, mixinClassName, mixinInfo);
    }
}
