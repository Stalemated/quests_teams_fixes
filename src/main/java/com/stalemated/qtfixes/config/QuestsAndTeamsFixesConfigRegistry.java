package com.stalemated.qtfixes.config;

import com.stalemated.lib.config.LocalConfigManager;
import com.stalemated.lib.config.SLibConfig;
import com.stalemated.qtfixes.QuestsAndTeamsFixes;

public final class QuestsAndTeamsFixesConfigRegistry {
    public static final LocalConfigManager<QuestsAndTeamsFixesConfig> CONFIG = SLibConfig.local(QuestsAndTeamsFixes.MOD_ID, QuestsAndTeamsFixesConfig.class).build();

    private QuestsAndTeamsFixesConfigRegistry() {
    }

    public static void register() {
        CONFIG.register();
    }
}
