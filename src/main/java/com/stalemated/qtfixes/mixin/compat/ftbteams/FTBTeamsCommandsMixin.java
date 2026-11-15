package com.stalemated.qtfixes.mixin.compat.ftbteams;

import com.mojang.brigadier.CommandDispatcher;
import com.stalemated.lib.mixin.RequiresMod;
import dev.ftb.mods.ftbteams.data.FTBTeamsCommands;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@RequiresMod("ftbteams")
@Mixin(value = FTBTeamsCommands.class)
public class FTBTeamsCommandsMixin {

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private void disableFTBTeamsCommands(CommandDispatcher<CommandSourceStack> dispatcher, CallbackInfo ci) {
        ci.cancel();
    }
}
