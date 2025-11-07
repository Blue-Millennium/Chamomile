package fun.bm.chamomile.function.modules;

import fun.bm.chamomile.config.modules.Bot.CoreConfig;
import fun.bm.chamomile.function.Function;
import fun.bm.chamomile.util.MainEnv;
import fun.bm.chamomile.util.helper.MainThreadHelper;
import org.bukkit.Bukkit;

import static fun.bm.chamomile.util.MainEnv.LOGGER;

public class QQBotKeeper extends Function {
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
        if (flag_continue && MainThreadHelper.isBotRunning() && !MainEnv.BOT.isOnline()) {
            if (MainThreadHelper.isBotRunning()) {
                MainEnv.BOT.login();
                LOGGER.warning("Trying to re-login BOT, result: " + (MainEnv.BOT.isOnline() ? "success" : "fault"));
            }
        }
    }

    public void setModuleName() {
        if (!CoreConfig.enabled && CoreConfig.loginCheckPeriod > 0) {
            this.moduleName = null;
        }
    }
}
