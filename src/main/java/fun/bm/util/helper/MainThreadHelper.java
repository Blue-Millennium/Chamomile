package fun.bm.util.helper;

import fun.bm.config.Config;
import fun.bm.config.ConfigManager;
import net.mamoe.mirai.event.GlobalEventChannel;
import org.bukkit.plugin.java.JavaPlugin;
import top.mrxiaom.overflow.BotBuilder;

import java.io.File;
import java.io.IOException;

import static fun.bm.Chamomile.*;

public class MainThreadHelper extends JavaPlugin {
    public static void Boot_QQBot() {
        if (BOT != null) {
            BOT.close();
        }
        if (Config.QQRobotEnabled) {
            BOT = BotBuilder.positive(Config.BotWsUrl).token(Config.BotWsToken).connect(); // 连接 LLOneBot
            eventChannel = GlobalEventChannel.INSTANCE;
            if (BOT == null) {
                Config.QQRobotEnabled = false;
                ConfigManager manager = new ConfigManager();
                manager.save();
                LOGGER.warning("Failed to get bot instance");
            }
        } else {
            LOGGER.info("QQ Robot has been disabled in this running regin.");
        }
    }

    public static void SetupDirectories() {
        if (!BASE_DIR.exists()) {
            for (File file : OLD_BASE_DIR) {
                if (file.exists()) {
                    try {
                        DirectoryExecutor.copyDirectory(file, BASE_DIR);
                        LOGGER.info("复制配置文件完成");
                        DirectoryExecutor.deleteDirectory(file);
                        LOGGER.info("删除旧配置文件完成");
                    } catch (IOException e) {
                        LOGGER.warning("复制配置文件时出现异常: " + e.getMessage());
                    }
                }
            }
            if (!BASE_DIR.mkdir()) {
                LOGGER.warning("Failed to create directory: " + BASE_DIR.getAbsolutePath());
            }
        }
        if (!DATA_FILE.exists()) {
            try {
                if (!DATA_FILE.createNewFile()) {
                    LOGGER.warning("Failed to create data file");
                }
            } catch (Exception e) {
                LOGGER.warning("Failed to create data file " + e.getMessage());
            }
        }
        if (!UNION_BAN_DATA_FILE.exists()) {
            try {
                if (!UNION_BAN_DATA_FILE.createNewFile()) {
                    LOGGER.warning("Failed to create unionban data file");
                }
            } catch (Exception e) {
                LOGGER.warning("Failed to create unionban data file " + e.getMessage());
            }
        }
    }
}
