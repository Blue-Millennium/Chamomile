package fun.xd.suka;

import fun.suya.suisuroru.commands.CommandRegister;
import fun.suya.suisuroru.config.Config;
import fun.suya.suisuroru.config.ConfigManager;
import fun.suya.suisuroru.data.DirectoryActions;
import fun.suya.suisuroru.message.DefaultMessages;
import fun.suya.suisuroru.module.impl.UnionBanModule;
import fun.xd.suka.data.DataManager;
import fun.xd.suka.module.ModuleManager;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.LoggerAdapters;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import top.mrxiaom.overflow.BotBuilder;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Liycxc
 * Date: 2024/7/16 下午8:38
 */
public class Main extends JavaPlugin implements Listener {
    public static Logger LOGGER = null;
    public static Main INSTANCE = null;
    public static Bot BOT;
    public static File OLD_BASE_DIR = new File("BasePlugin");
    public static File BASE_DIR = new File("plugins/BasePlugin");
    public static File DATA_FILE = new File(BASE_DIR, "data.json");
    public static File CONFIG_FILE = new File(BASE_DIR, "config.properties");
    public static File REPORT_DATA_FILE = new File(BASE_DIR, "report.csv");
    public DataManager dataManager;
    public ConfigManager configManager;
    public ModuleManager moduleManager;
    public GlobalEventChannel eventChannel;

    @Override
    public void onLoad() {
        if (INSTANCE == null) {
            INSTANCE = this;
            LOGGER = INSTANCE.getLogger();
        }

        dataManager = new DataManager();
        configManager = new ConfigManager();
        moduleManager = new ModuleManager();

        if (!BASE_DIR.exists()) {
            if (OLD_BASE_DIR.exists()) {
                try {
                    DirectoryActions.copyDirectory(OLD_BASE_DIR, BASE_DIR);
                    LOGGER.info("复制配置文件完成");
                    DirectoryActions.deleteDirectory(OLD_BASE_DIR);
                    LOGGER.info("删除旧配置文件完成");
                } catch (IOException e) {
                    LOGGER.warning("复制配置文件时出现异常: " + e.getMessage());
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

        dataManager.load();
        configManager.load();
        moduleManager.load();
        UnionBanModule.EnableUnionBan(this);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this); // 注册事件

        LoggerAdapters.useLog4j2(); // 使用 Log4j2 作为日志记录器
        if (Config.QQRobotEnabled) {
            BOT = BotBuilder.positive(Config.botWsUrl).token(Config.botWsToken).connect(); // 连接 LLOneBot
            eventChannel = GlobalEventChannel.INSTANCE;
            if (BOT == null) {
                LOGGER.warning("Failed to get bot instance");
                return;
            }
        } else {
            LOGGER.info("QQ Robot has been disabled in this running regin.");
        }
        DefaultMessages.TurnOnPlugin();
        CommandRegister.registerCommand(this);
        moduleManager.onEnable();
    }

    @Override
    public void onDisable() {
        DefaultMessages.TurnOffPlugin();
        moduleManager.onDisable();
    }
}
