package fun.bm.chamomile.util.helper;

import fun.bm.chamomile.Chamomile;
import fun.bm.chamomile.config.modules.Bot.CoreConfig;
import fun.bm.chamomile.util.MainEnv;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.LoggerAdapters;
import top.mrxiaom.overflow.BotBuilder;

import java.util.concurrent.CompletableFuture;

import static fun.bm.chamomile.util.MainEnv.LOGGER;

public class MainThreadHelper {
    public static CompletableFuture<Void> botFuture = null;

    public static boolean isBotRunning() {
        return botFuture != null && botFuture.isDone();
    }

    public static void bootQQBot() {
        botFuture = CompletableFuture.runAsync(() -> {
            LoggerAdapters.useLog4j2(); // 使用 Log4j2 作为日志记录器
            if (MainEnv.BOT != null) {
                MainEnv.BOT.close();
            }
            if (CoreConfig.enabled) {
                MainEnv.BOT = BotBuilder.positive(CoreConfig.wsUrl).token(CoreConfig.wsToken).noPlatform().modifyBotConfiguration(config -> {
                    config.disableContactCache();
                    config.autoReconnectOnForceOffline();
                }).connect(); // 连接 LLOneBot
                MainEnv.eventChannel = GlobalEventChannel.INSTANCE;
                if (MainEnv.BOT == null) {
                    MainEnv.configManager.setConfigAndSave("bot.core.enabled", false);
                    LOGGER.warning("Failed to get bot instance");
                }
            } else {
                LOGGER.info("QQ Robot has been disabled in this running regin.");
            }
        });
    }

    public static void setupBaseEnv(Chamomile plugin) {
        if (MainEnv.INSTANCE == null) {
            MainEnv.INSTANCE = plugin;
            MainEnv.LOGGER = MainEnv.INSTANCE.getLogger();
        }
        setupDirectories();
        MainEnv.dataManager.load();
        MainEnv.configManager.load();
        MainEnv.functionManager.load();
    }

    private static void setupDirectories() {
        if (!MainEnv.BASE_DIR.exists()) {
            if (!MainEnv.BASE_DIR.mkdir()) {
                LOGGER.warning("Failed to create directory: " + MainEnv.BASE_DIR.getAbsolutePath());
            }
        }
    }
}
