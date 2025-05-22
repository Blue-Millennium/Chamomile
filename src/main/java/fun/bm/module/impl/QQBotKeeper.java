package fun.bm.module.impl;

import fun.bm.config.modules.Bot.CoreConfig;
import fun.bm.module.Module;
import fun.bm.util.MainEnv;
import org.bukkit.Bukkit;

import static fun.bm.util.MainEnv.LOGGER;

public class QQBotKeeper extends Module {
    boolean flag_continue = true;

    public QQBotKeeper() {
        super("BotKeeper");
    }

    public void onEnable() {
        if (CoreConfig.enabled && CoreConfig.loginCheckPeriod > 0)
            Bukkit.getScheduler().runTaskLater(MainEnv.INSTANCE, this::scheduleTask, CoreConfig.loginCheckPeriod * 20L);
    }

    public void onDisable() {
        flag_continue = false;
    }

    public void scheduleTask() {
        if (flag_continue && !MainEnv.BOT.isOnline()) {
            MainEnv.BOT.login();
            LOGGER.warning("Trying to re-login BOT, result: " + (MainEnv.BOT.isOnline() ? "success" : "fault"));
        }
    }

    public void setModuleName() {
        if (!CoreConfig.enabled && CoreConfig.loginCheckPeriod > 0) {
            this.moduleName = null;
        }
    }
}
