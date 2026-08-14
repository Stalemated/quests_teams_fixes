package com.stalemated.qtfixes.mixin;

import dev.ftb.mods.ftblibrary.sidebar.SidebarButtonManager;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SidebarButtonManager.class)
public abstract class SidebarButtonManagerMixin {

    @Inject(method = "reload", at = @At("HEAD"), cancellable = true)
    private void removeFTBTeamsButtons(ResourceManager manager, CallbackInfo ci) {
        ci.cancel();
    }
}
