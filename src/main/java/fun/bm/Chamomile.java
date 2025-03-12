package fun.bm;

import fun.bm.command.CommandManager;
import fun.bm.config.ConfigManager;
import fun.bm.data.PlayerData.DataManager;
import fun.bm.module.ModuleManager;
import fun.bm.util.helper.EmailSender;
import fun.bm.util.helper.MainThreadHelper;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.LoggerAdapters;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
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

    @Override
    public void onLoad() {
        if (INSTANCE == null) {
            INSTANCE = this;
            LOGGER = INSTANCE.getLogger();
        }

        configManager = new ConfigManager();
        dataManager = new DataManager();
        moduleManager = new ModuleManager();

        MainThreadHelper.SetupDirectories();

        dataManager.load();
        configManager.load();
        moduleManager.setupModules(true);
    }

    @Override
    public void onEnable() {
        EmailSender.TurnPlugin("启动");
        // 启动逻辑 - Start
        Bukkit.getPluginManager().registerEvents(this, this); // 注册事件
        LoggerAdapters.useLog4j2(); // 使用 Log4j2 作为日志记录器
        MainThreadHelper.Boot_QQBot();
        CommandManager.registerCommand();
        moduleManager.onEnable();
        // 启动逻辑 - End
        EmailSender.CheckPlugin("启动");
    }

    @Override
    public void onDisable() {
        EmailSender.TurnPlugin("关闭");
        // 关闭逻辑 - Start
        moduleManager.onDisable();
        // 关闭逻辑 - End
        EmailSender.CheckPlugin("关闭");
    }
}
