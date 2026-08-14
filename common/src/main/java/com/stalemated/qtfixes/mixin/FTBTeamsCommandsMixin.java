package com.stalemated.qtfixes.mixin;

import com.mojang.brigadier.CommandDispatcher;
import dev.ftb.mods.ftbteams.data.FTBTeamsCommands;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FTBTeamsCommands.class)
public class FTBTeamsCommandsMixin {

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private void disableFTBTeamsCommands(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci) {
        ci.cancel();
    }
}
