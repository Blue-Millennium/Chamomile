package fun.bm.chamomile.util.helper;

import fun.bm.chamomile.Chamomile;
import fun.bm.chamomile.config.modules.Bot.CoreConfig;
import fun.bm.chamomile.config.modules.ServerConfig;
import fun.bm.chamomile.config.modules.WebhookConfig;
import fun.bm.chamomile.util.Environment;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.LoggerAdapters;
import top.mrxiaom.overflow.BotBuilder;

import java.util.concurrent.CompletableFuture;

import static fun.bm.chamomile.util.Environment.LOGGER;

public class MainThreadHelper {
    public static CompletableFuture<Void> botFuture = null;

    public static boolean isBotRunning() {
        return botFuture != null && botFuture.isDone();
    }

    public static void bootQQBot() {
        botFuture = CompletableFuture.runAsync(() -> {
            LoggerAdapters.useLog4j2(); // 使用 Log4j2 作为日志记录器
            if (Environment.BOT != null) {
                Environment.BOT.close();
            }
            if (CoreConfig.enabled) {
                Environment.BOT = BotBuilder.positive(CoreConfig.wsUrl).token(CoreConfig.wsToken).noPlatform().modifyBotConfiguration(config -> {
                    config.disableContactCache();
                    config.autoReconnectOnForceOffline();
                }).connect(); // 连接 LLOneBot
                Environment.eventChannel = GlobalEventChannel.INSTANCE;
                if (Environment.BOT == null) {
                    Environment.configManager.setConfigAndSave("bot.core.enabled", false);
                    LOGGER.warning("Failed to get bot instance");
                }
            } else {
                LOGGER.info("QQ Robot has been disabled in this running regin.");
            }
        });
    }

    public static void setupBaseEnv(Chamomile plugin) {
        if (Environment.INSTANCE == null) {
            Environment.INSTANCE = plugin;
            Environment.LOGGER = Environment.INSTANCE.getLogger();
        }
        setupDirectories();
        Environment.dataManager.load();
        Environment.configManager.load();
        Environment.functionManager.load();
    }

    private static void setupDirectories() {
        if (!Environment.BASE_DIR.exists()) {
            if (!Environment.BASE_DIR.mkdir()) {
                LOGGER.warning("Failed to create directory: " + Environment.BASE_DIR.getAbsolutePath());
            }
        }
    }

    public static void checkPlugin(String title) {
        try {
            String subject = "服务器" + title + "通知";
            String content = ServerConfig.serverName + "服务器已" + title + "完成";
            EmailSender.formatAndSendWebhook(subject, content, WebhookConfig.webHookEmails);
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
    }
}
