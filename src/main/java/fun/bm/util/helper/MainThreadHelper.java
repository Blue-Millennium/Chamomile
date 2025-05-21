package fun.bm.util.helper;

import fun.bm.Chamomile;
import fun.bm.config.old.Config;
import fun.bm.util.MainEnv;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.LoggerAdapters;
import top.mrxiaom.overflow.BotBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static fun.bm.util.MainEnv.LOGGER;

public class MainThreadHelper {
    static List<String> oldDir = List.of("BasePlugin", "plugins/BasePlugin");

    public static void bootQQBot() {
        LoggerAdapters.useLog4j2(); // 使用 Log4j2 作为日志记录器
        if (MainEnv.BOT != null) {
            MainEnv.BOT.close();
        }
        if (Config.QQRobotEnabled) {
            MainEnv.BOT = BotBuilder.positive(Config.BotWsUrl).token(Config.BotWsToken).connect(); // 连接 LLOneBot
            MainEnv.eventChannel = GlobalEventChannel.INSTANCE;
            if (MainEnv.BOT == null) {
                Config.QQRobotEnabled = false;
                MainEnv.configManager.save();
                LOGGER.warning("Failed to get bot instance");
            }
        } else {
            LOGGER.info("QQ Robot has been disabled in this running regin.");
        }
    }

    public static void setupBaseEnv(Chamomile plugin) {
        initOldDirectory();
        if (MainEnv.INSTANCE == null) {
            MainEnv.INSTANCE = plugin;
            MainEnv.LOGGER = MainEnv.INSTANCE.getLogger();
        }
        setupDirectories();
        MainEnv.dataManager.load();
        MainEnv.configManager.load();
        MainEnv.moduleManager.load();
    }

    private static void setupDirectories() {
        if (!MainEnv.BASE_DIR.exists()) {
            for (File file : MainEnv.OLD_BASE_DIR) {
                if (file.exists()) {
                    try {
                        DirectoryAccessor.copyDirectory(file, MainEnv.BASE_DIR);
                        LOGGER.info("复制配置文件完成");
                        DirectoryAccessor.deleteDirectory(file);
                        LOGGER.info("删除旧配置文件完成");
                    } catch (IOException e) {
                        LOGGER.warning("复制配置文件时出现异常: " + e.getMessage());
                    }
                }
            }
            if (!MainEnv.BASE_DIR.mkdir()) {
                LOGGER.warning("Failed to create directory: " + MainEnv.BASE_DIR.getAbsolutePath());
            }
        }
        if (!MainEnv.DATA_FILE.exists()) {
            try {
                if (!MainEnv.DATA_FILE.createNewFile()) {
                    LOGGER.warning("Failed to create data file");
                }
            } catch (Exception e) {
                LOGGER.warning("Failed to create data file " + e.getMessage());
            }
        }
        if (!MainEnv.UNION_BAN_DATA_FILE.exists()) {
            try {
                if (!MainEnv.UNION_BAN_DATA_FILE.createNewFile()) {
                    LOGGER.warning("Failed to create unionban data file");
                }
            } catch (Exception e) {
                LOGGER.warning("Failed to create unionban data file " + e.getMessage());
            }
        }
    }

    public static void initOldDirectory() {
        List<File> dir = new java.util.ArrayList<>();
        for (String s : oldDir) {
            dir.add(new File(s));
        }
        MainEnv.OLD_BASE_DIR = dir;
    }
}
