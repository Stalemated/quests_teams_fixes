package com.stalemated.qtfixes.mixin.compat.ftbteams;

import com.stalemated.lib.mixin.RequiresMod;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftbteams.data.PartyTeam;
import dev.ftb.mods.ftbteams.data.TeamManagerImpl;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@RequiresMod("ftbteams")
@Mixin(value = TeamManagerImpl.class)
public class TeamManagerImplMixin {

    @Inject(method = "createParty(Ljava/util/UUID;Lnet/minecraft/server/level/ServerPlayer;Ljava/lang/String;Ljava/lang/String;Ldev/ftb/mods/ftblibrary/icon/Color4I;)Ldev/ftb/mods/ftbteams/data/PartyTeam;", at = @At("HEAD"), cancellable = true)
    private void disablePartyCreation(UUID playerId, ServerPlayer player, String name, String description, Color4I color, CallbackInfoReturnable<PartyTeam> cir) {
        cir.cancel();
    }
}
