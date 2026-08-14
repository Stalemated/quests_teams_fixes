package com.stalemated.qtfixes.mixin;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftbteams.data.PartyTeam;
import dev.ftb.mods.ftbteams.data.TeamManagerImpl;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(value = TeamManagerImpl.class)
public class TeamManagerImplMixin {

    @Inject(method = "createParty(Ljava/util/UUID;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/lang/String;Ljava/lang/String;Ldev/ftb/mods/ftblibrary/icon/Color4I;)Ldev/ftb/mods/ftbteams/data/PartyTeam;", at = @At("HEAD"), cancellable = true)
    private void disablePartyCreation(UUID playerId, ServerPlayerEntity player, String name, String description, Color4I color, CallbackInfoReturnable<PartyTeam> cir) {
        cir.cancel();
    }
}
