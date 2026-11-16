package com.stalemated.qtfixes.mixin.compat.ftblibrary;

import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.qtfixes.config.ActiveFixes;
import dev.ftb.mods.ftblibrary.sidebar.SidebarButtonManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.1 {
/*import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;
*///?}

@RequiresMod("ftblibrary")
@Mixin(value = SidebarButtonManager.class)
public abstract class SidebarButtonManagerMixin {

    //? if <1.21.1 {
    @Inject(method = "onResourceManagerReload", at = @At("HEAD"), cancellable = true)
    private void quests_teams_fixes$hideAllSidebarButtons(ResourceManager manager, CallbackInfo ci) {
        if (ActiveFixes.current().hideSidebarButtons()) ci.cancel();
    }
    //?} else {
    /*@Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"), cancellable = true)
    private void quests_teams_fixes$hideAllSidebarButtons(Map<ResourceLocation, JsonElement> buttons, ResourceManager manager, ProfilerFiller profiler, CallbackInfo ci) {
        if (ActiveFixes.current().hideSidebarButtons()) ci.cancel();
    }
    *///?}
}
