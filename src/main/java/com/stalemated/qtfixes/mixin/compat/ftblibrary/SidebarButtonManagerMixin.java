package com.stalemated.qtfixes.mixin.compat.ftblibrary;

import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.qtfixes.config.ActiveFixes;
import dev.ftb.mods.ftblibrary.sidebar.SidebarButtonManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@RequiresMod("ftblibrary")
@Mixin(value = SidebarButtonManager.class)
public abstract class SidebarButtonManagerMixin {

    @Inject(method = "onResourceManagerReload", at = @At("HEAD"), cancellable = true)
    private void quests_teams_fixes$hideAllSidebarButtons(ResourceManager manager, CallbackInfo ci) {
        if (ActiveFixes.current().hideSidebarButtons()) ci.cancel();
    }
}
