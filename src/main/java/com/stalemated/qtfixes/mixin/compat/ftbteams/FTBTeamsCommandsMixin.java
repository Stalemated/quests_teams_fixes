package com.stalemated.qtfixes.mixin.compat.ftbteams;

import com.mojang.brigadier.CommandDispatcher;
import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.qtfixes.config.ActiveFixes;
//? if >=1.21.11 {
/*import dev.ftb.mods.ftbteams.command.FTBTeamsCommands;
*///?} else
import dev.ftb.mods.ftbteams.data.FTBTeamsCommands;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@RequiresMod("ftbteams")
@Mixin(value = FTBTeamsCommands.class)
public abstract class FTBTeamsCommandsMixin {

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private void quests_teams_fixes$skipFtbTeamsCommands(CommandDispatcher<CommandSourceStack> dispatcher, CallbackInfo ci) {
        if (ActiveFixes.current().removeFtbTeamsCommand()) ci.cancel();
    }
}
