package fun.bm.util.helper;

import fun.bm.Chamomile;
import fun.bm.config.Config;
import fun.bm.config.ConfigManager;
import net.mamoe.mirai.event.GlobalEventChannel;
import org.bukkit.plugin.java.JavaPlugin;
import top.mrxiaom.overflow.BotBuilder;

import java.io.File;
import java.io.IOException;

public class MainThreadHelper extends JavaPlugin {
    public static void Boot_QQBot() {
        if (Chamomile.BOT != null) {
            Chamomile.BOT.close();
        }
        if (Config.QQRobotEnabled) {
            Chamomile.BOT = BotBuilder.positive(Config.BotWsUrl).token(Config.BotWsToken).connect(); // 连接 LLOneBot
            Chamomile.eventChannel = GlobalEventChannel.INSTANCE;
            if (Chamomile.BOT == null) {
                Config.QQRobotEnabled = false;
                ConfigManager manager = new ConfigManager();
                manager.save();
                Chamomile.LOGGER.warning("Failed to get bot instance");
            }
        } else {
            Chamomile.LOGGER.info("QQ Robot has been disabled in this running regin.");
        }
    }

    public static void SetupDirectories() {
        if (!Chamomile.BASE_DIR.exists()) {
            for (File file : Chamomile.OLD_BASE_DIR) {
                if (file.exists()) {
                    try {
                        DirectoryExecutor.copyDirectory(file, Chamomile.BASE_DIR);
                        Chamomile.LOGGER.info("复制配置文件完成");
                        DirectoryExecutor.deleteDirectory(file);
                        Chamomile.LOGGER.info("删除旧配置文件完成");
                    } catch (IOException e) {
                        Chamomile.LOGGER.warning("复制配置文件时出现异常: " + e.getMessage());
                    }
                }
            }
            if (!Chamomile.BASE_DIR.mkdir()) {
                Chamomile.LOGGER.warning("Failed to create directory: " + Chamomile.BASE_DIR.getAbsolutePath());
            }
        }
        if (!Chamomile.DATA_FILE.exists()) {
            try {
                if (!Chamomile.DATA_FILE.createNewFile()) {
                    Chamomile.LOGGER.warning("Failed to create data file");
                }
            } catch (Exception e) {
                Chamomile.LOGGER.warning("Failed to create data file " + e.getMessage());
            }
        }

        if (!Chamomile.UNION_BAN_DATA_FILE.exists()) {
            try {
                if (!Chamomile.UNION_BAN_DATA_FILE.createNewFile()) {
                    Chamomile.LOGGER.warning("Failed to create unionban data file");
                }
            } catch (Exception e) {
                Chamomile.LOGGER.warning("Failed to create unionban data file " + e.getMessage());
            }
        }
    }
}
