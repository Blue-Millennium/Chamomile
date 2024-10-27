package xd.suka;

import fun.suya.suisuroru.commands.execute.CommandManager;
import fun.suya.suisuroru.commands.execute.othercommands.ConfigRoot;
import fun.suya.suisuroru.commands.execute.othercommands.Help;
import fun.suya.suisuroru.commands.execute.othercommands.ReportQuery;
import fun.suya.suisuroru.commands.execute.othercommands.config.Reload;
import fun.suya.suisuroru.commands.execute.othercommands.vanilla.Ban;
import fun.suya.suisuroru.commands.execute.othercommands.vanilla.Pardon;
import fun.suya.suisuroru.commands.tab.BasePluginTab;
import fun.suya.suisuroru.commands.tab.BpconfigTab;
import fun.suya.suisuroru.commands.tab.ReportCommandTab;
import fun.suya.suisuroru.commands.tab.vanilla.BanTab;
import fun.suya.suisuroru.commands.tab.vanilla.PardonTab;
import fun.suya.suisuroru.config.Config;
import fun.suya.suisuroru.config.ConfigManager;
import fun.suya.suisuroru.message.Webhook4Email;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.LoggerAdapters;
import org.bukkit.Bukkit;
import org.bukkit.entity.Minecart;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import top.mrxiaom.overflow.BotBuilder;
import xd.suka.command.ReportCommand;
import xd.suka.data.DataManager;
import xd.suka.module.ModuleManager;

import java.io.File;
import java.util.logging.Logger;

/**
 * @author Liycxc
 * Date: 2024/7/16 下午8:38
 */
public class Main extends JavaPlugin implements Listener {
    public static Logger LOGGER = null;
    public static Main INSTANCE = null;

    public File BASE_DIR = new File("BasePlugin");
    public File DATA_FILE = new File(BASE_DIR, "data.json");
    public File CONFIG_FILE = new File(BASE_DIR, "config.properties");
    public DataManager dataManager;
    public ConfigManager configManager;
    public ModuleManager moduleManager;
    public GlobalEventChannel eventChannel;

    public Bot BOT;

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
            if (!BASE_DIR.mkdir()) {
                LOGGER.warning("Failed to create directory: " + BASE_DIR.getAbsolutePath());
            }
        }
        if (!DATA_FILE.exists()) {
            try {
                if (!DATA_FILE.createNewFile()) {
                    LOGGER.warning("Failed to create data file");
                }
            } catch (Exception exception) {
                LOGGER.warning("Failed to create data file " + exception.getMessage());
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
        try {
            String subject = "服务器启动通知";
            String content = Config.servername + "服务器已启动";
            Webhook4Email webhook4Email = new Webhook4Email();
            webhook4Email.formatAndSendWebhook(subject, content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        moduleManager.onEnable();

        // vanilla functions
        this.getCommand("ban").setExecutor(new Ban());
        this.getCommand("pardon").setExecutor(new Pardon());
        this.getCommand("ban").setTabCompleter(new BanTab());
        this.getCommand("pardon").setTabCompleter(new PardonTab());

        // new functions
        this.getCommand("basepluginhelp").setExecutor(new Help());
        this.getCommand("report").setExecutor(new ReportCommand());
        this.getCommand("report").setTabCompleter(new ReportCommandTab());
        this.getCommand("bpconfig").setExecutor(new ConfigRoot());
        this.getCommand("bpconfig").setTabCompleter(new BpconfigTab());
        this.getCommand("bpreload").setExecutor(new Reload());
        this.getCommand("baseplugin").setExecutor(new CommandManager());
        this.getCommand("baseplugin").setTabCompleter(new BasePluginTab());
        this.getCommand("query-report").setExecutor(new ReportQuery());
    }

    @Override
    public void onDisable() {
        try {
            String subject = "服务器关闭通知";
            String content = Config.servername + "服务器已关闭";
            Webhook4Email webhook4Email = new Webhook4Email();
            webhook4Email.formatAndSendWebhook(subject, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        moduleManager.onDisable();
    }
}
