package com.stalemated.qtfixes;

import com.stalemated.lib.helper.PlatformHelper;
import com.stalemated.qtfixes.migration.ExistingParties;
import com.stalemated.qtfixes.party.PartyCreation;
//? if <1.21.1
import com.stalemated.qtfixes.completion.StuckFlexibleQuests;

//? if fabric {
import com.stalemated.lib.util.ResourceIds;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resources.ResourceLocation;
//? if <1.21.1
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
//?} elif forge {
/*import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
*///?} elif neoforge {
/*import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
//? if <1.21.1
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
*///?}

//? if (forge || neoforge) && <1.21.1
//import net.minecraft.server.level.ServerPlayer;

final class FtbServerHooks {
    //? if fabric
    private static final ResourceLocation AFTER_FTB_QUESTS_LOADS = ResourceIds.of(QuestsAndTeamsFixes.MOD_ID, "after_ftb_quests_loads");
    //? if forge
    //private static final IEventBus GAME_EVENTS = MinecraftForge.EVENT_BUS;
    //? if neoforge
    //private static final IEventBus GAME_EVENTS = NeoForge.EVENT_BUS;

    private FtbServerHooks() {
    }

    static void registerIfFtbIsLoaded() {
        if (!areFtbModsLoaded()) return;

        //? if fabric {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> PartyCreation.restrictFtbTeamsToApi());
        ServerLifecycleEvents.SERVER_STARTED.addPhaseOrdering(Event.DEFAULT_PHASE, AFTER_FTB_QUESTS_LOADS);
        ServerLifecycleEvents.SERVER_STARTED.register(AFTER_FTB_QUESTS_LOADS, ExistingParties::serverStarted);
        //? if <1.21.1
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> StuckFlexibleQuests.playerJoined(handler.player));
        //?} else {
        /*GAME_EVENTS.addListener(FtbServerHooks::onServerStarting);
        GAME_EVENTS.addListener(EventPriority.LOWEST, FtbServerHooks::onServerStarted);
        //? if <1.21.1
        GAME_EVENTS.addListener((PlayerEvent.PlayerLoggedInEvent event) -> StuckFlexibleQuests.playerJoined((ServerPlayer) event.getEntity()));
        *///?}
    }

    private static boolean areFtbModsLoaded() {
        return PlatformHelper.isModLoaded("ftbquests") &&
                PlatformHelper.isModLoaded("ftbteams") &&
                PlatformHelper.isModLoaded("ftblibrary");
    }

    //? if forge || neoforge {
    /*private static void onServerStarting(ServerStartingEvent event) {
        PartyCreation.restrictFtbTeamsToApi();
    }

    private static void onServerStarted(ServerStartedEvent event) {
        ExistingParties.serverStarted(event.getServer());
    }
    *///?}
}
