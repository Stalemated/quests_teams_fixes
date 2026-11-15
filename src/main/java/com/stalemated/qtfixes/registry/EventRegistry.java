package com.stalemated.qtfixes.registry;

import com.stalemated.lib.helper.PlatformHelper;
import com.stalemated.qtfixes.completion.StuckFlexibleQuests;
import com.stalemated.qtfixes.migration.PartyMigrationHandler;

//? if fabric {
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
//?} elif forge {
/*import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
*///?}

public class EventRegistry {
    public static void register() {
        if (areFtbModsLoaded()) {
            //? if fabric {
            ServerLifecycleEvents.SERVER_STARTED.register(PartyMigrationHandler::onServerStarted);
            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> StuckFlexibleQuests.playerJoined(handler.player));
            //?} elif forge {
            /*MinecraftForge.EVENT_BUS.addListener(EventRegistry::onServerStarted);
            MinecraftForge.EVENT_BUS.addListener(EventRegistry::onPlayerLoggedIn);
            *///?}
        }
    }

    private static boolean areFtbModsLoaded() {
        return PlatformHelper.isModLoaded("ftbquests") &&
                PlatformHelper.isModLoaded("ftbteams") &&
                PlatformHelper.isModLoaded("ftblibrary");
    }

    //? if forge {
    /*private static void onServerStarted(ServerStartedEvent event) {
        PartyMigrationHandler.onServerStarted(event.getServer());
    }

    private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        StuckFlexibleQuests.playerJoined((ServerPlayer) event.getEntity());
    }
    *///?}
}
