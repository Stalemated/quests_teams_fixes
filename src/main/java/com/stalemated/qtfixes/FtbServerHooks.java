package com.stalemated.qtfixes;

import com.stalemated.lib.helper.PlatformHelper;
import com.stalemated.qtfixes.completion.StuckFlexibleQuests;
import com.stalemated.qtfixes.migration.ExistingParties;

//? if fabric {
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
//?} elif forge {
/*import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
*///?}

final class FtbServerHooks {

    private FtbServerHooks() {
    }

    static void registerIfFtbIsLoaded() {
        if (!areFtbModsLoaded()) return;

        //? if fabric {
        ServerLifecycleEvents.SERVER_STARTED.register(ExistingParties::serverStarted);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> StuckFlexibleQuests.playerJoined(handler.player));
        //?} elif forge {
        /*MinecraftForge.EVENT_BUS.addListener(FtbServerHooks::onServerStarted);
        MinecraftForge.EVENT_BUS.addListener(FtbServerHooks::onPlayerLoggedIn);
        *///?}
    }

    private static boolean areFtbModsLoaded() {
        return PlatformHelper.isModLoaded("ftbquests") &&
                PlatformHelper.isModLoaded("ftbteams") &&
                PlatformHelper.isModLoaded("ftblibrary");
    }

    //? if forge {
    /*private static void onServerStarted(ServerStartedEvent event) {
        ExistingParties.serverStarted(event.getServer());
    }

    private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        StuckFlexibleQuests.playerJoined((ServerPlayer) event.getEntity());
    }
    *///?}
}
