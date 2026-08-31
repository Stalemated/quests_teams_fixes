package com.stalemated.qtfixes.mixin.compat;

import com.stalemated.lib.helper.PlatformHelper;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class QTFMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("compat.ftbteams")) {
            return PlatformHelper.INSTANCE.isModLoadedAtLaunch("ftbteams");
        }
        if (mixinClassName.contains("compat.ftbquests")) {
            return PlatformHelper.INSTANCE.isModLoadedAtLaunch("ftbquests");
        }
        if (mixinClassName.contains("compat.ftblibrary")) {
            return PlatformHelper.INSTANCE.isModLoadedAtLaunch("ftblibrary");
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}