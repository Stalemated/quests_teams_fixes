package com.stalemated.qtfixes;

import com.stalemated.qtfixes.config.QuestsAndTeamsFixesConfigRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? if fabric {
import net.fabricmc.api.ModInitializer;
//?} elif forge {
/*import net.minecraftforge.fml.common.Mod;
*///?}

//? if forge {
/*@Mod(QuestsAndTeamsFixes.MOD_ID)
*///?}
public final class QuestsAndTeamsFixes //? if fabric
implements ModInitializer
{
    public static final String MOD_ID = "quests_teams_fixes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    //? if fabric {
    @Override
    public void onInitialize() {
        init();
    }
    //?} elif forge {
    /*public QuestsAndTeamsFixes() {
        init();
    }
    *///?}

    private static void init() {
        QuestsAndTeamsFixesConfigRegistry.register();
        FtbServerHooks.registerIfFtbIsLoaded();
    }
}
