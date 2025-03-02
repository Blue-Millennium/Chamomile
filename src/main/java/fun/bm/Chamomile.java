package fun.bm;

import fun.bm.command.manager.CommandManager;
import fun.bm.config.Config;
import fun.bm.config.ConfigManager;
import fun.bm.data.DataManager;
import fun.bm.message.DefaultMessages;
import fun.bm.module.ModuleManager;
import fun.bm.util.DirectoryActions;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.LoggerAdapters;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import top.mrxiaom.overflow.BotBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Liycxc
 * Date: 2024/7/16 下午8:38
 */
public class Chamomile extends JavaPlugin implements Listener {
    public static Logger LOGGER = null;
    public static Chamomile INSTANCE = null;
    public static Bot BOT;
    public static List<File> OLD_BASE_DIR = List.of(new File("BasePlugin"), new File("plugins/BasePlugin"));
    public static File BASE_DIR = new File("plugins/Chamomile");
    public static File DATA_FILE = new File(BASE_DIR, "data.json");
    public static File CONFIG_FILE = new File(BASE_DIR, "config.properties");
    public static File REPORT_DATA_FILE = new File(BASE_DIR, "report.csv");
    public static File UNION_BAN_DATA_FILE = new File(BASE_DIR, "unionbandata.json");
    public static GlobalEventChannel eventChannel;
    public DataManager dataManager;
    public ConfigManager configManager;
    public ModuleManager moduleManager;

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

    @Override
    public void onLoad() {
        if (INSTANCE == null) {
            INSTANCE = this;
            LOGGER = INSTANCE.getLogger();
        }

        configManager = new ConfigManager();
        dataManager = new DataManager();
        moduleManager = new ModuleManager();

        if (!BASE_DIR.exists()) {
            for (File file : OLD_BASE_DIR) {
                if (file.exists()) {
                    try {
                        DirectoryActions.copyDirectory(file, BASE_DIR);
                        LOGGER.info("复制配置文件完成");
                        DirectoryActions.deleteDirectory(file);
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

        dataManager.load();
        configManager.load();
        moduleManager.load();
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this); // 注册事件
        LoggerAdapters.useLog4j2(); // 使用 Log4j2 作为日志记录器
        Boot_QQBot();
        DefaultMessages.TurnOnPlugin();
        CommandManager.registerCommand(this);
        moduleManager.onEnable();
    }

    @Override
    public void onDisable() {
        DefaultMessages.TurnOffPlugin();
        moduleManager.onDisable();
    }
}
