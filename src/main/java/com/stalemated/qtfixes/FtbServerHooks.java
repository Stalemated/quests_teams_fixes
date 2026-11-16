package com.stalemated.qtfixes;

import com.stalemated.lib.helper.PlatformHelper;
import com.stalemated.qtfixes.completion.StuckFlexibleQuests;
import com.stalemated.qtfixes.migration.ExistingParties;
import com.stalemated.qtfixes.party.PartyCreation;

//? if fabric {
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.resources.ResourceLocation;
//?} elif forge {
/*import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
*///?}

final class FtbServerHooks {
    //? if fabric
    private static final ResourceLocation AFTER_FTB_QUESTS_LOADS = new ResourceLocation(QuestsAndTeamsFixes.MOD_ID, "after_ftb_quests_loads");

    private FtbServerHooks() {
    }

    static void registerIfFtbIsLoaded() {
        if (!areFtbModsLoaded()) return;

        //? if fabric {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> PartyCreation.restrictFtbTeamsToApi());
        ServerLifecycleEvents.SERVER_STARTED.addPhaseOrdering(Event.DEFAULT_PHASE, AFTER_FTB_QUESTS_LOADS);
        ServerLifecycleEvents.SERVER_STARTED.register(AFTER_FTB_QUESTS_LOADS, ExistingParties::serverStarted);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> StuckFlexibleQuests.playerJoined(handler.player));
        //?} elif forge {
        /*MinecraftForge.EVENT_BUS.addListener(FtbServerHooks::onServerStarting);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, FtbServerHooks::onServerStarted);
        MinecraftForge.EVENT_BUS.addListener(FtbServerHooks::onPlayerLoggedIn);
        *///?}
    }

    private static boolean areFtbModsLoaded() {
        return PlatformHelper.isModLoaded("ftbquests") &&
                PlatformHelper.isModLoaded("ftbteams") &&
                PlatformHelper.isModLoaded("ftblibrary");
    }

    //? if forge {
    /*private static void onServerStarting(ServerStartingEvent event) {
        PartyCreation.restrictFtbTeamsToApi();
    }

    private static void onServerStarted(ServerStartedEvent event) {
        ExistingParties.serverStarted(event.getServer());
    }

    private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        StuckFlexibleQuests.playerJoined((ServerPlayer) event.getEntity());
    }
    *///?}
}
