package xd.suka;

import net.kyori.adventure.text.Component;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.utils.LoggerAdapters;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.mrxiaom.overflow.BotBuilder;
import xd.suka.config.Config;
import xd.suka.config.ConfigManager;
import xd.suka.data.DataManager;
import xd.suka.module.ModuleManager;

import java.io.File;

/**
 * @author Liycxc
 * Date: 2024/7/16 下午8:38
 */
public class Main extends JavaPlugin implements Listener {
    public static final Logger LOGGER = LoggerFactory.getLogger("QQLogin");
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
        }

        dataManager = new DataManager();
        configManager = new ConfigManager();
        moduleManager = new ModuleManager();

        if (!BASE_DIR.exists()) {
            if (!BASE_DIR.mkdir()) {
                LOGGER.error("Failed to create directory: {}", BASE_DIR.getAbsolutePath());
            }
        }
        if (!DATA_FILE.exists()) {
            try {
                if (!DATA_FILE.createNewFile()) {
                    LOGGER.error("Failed to create data file");
                }
            } catch (Exception exception) {
                LOGGER.error("Failed to create data file", exception);
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
        BOT = BotBuilder.positive(Config.botWsUrl).token(Config.botWsToken).connect(); // 连接 LLOneBot
        eventChannel = GlobalEventChannel.INSTANCE;

        if (BOT == null) {
            LOGGER.error("Failed to get bot instance");
            return;
        }

        moduleManager.onEnable();

        Bukkit.getCommandMap().register("BasePlugin", "bp", new org.bukkit.command.Command("BasePlugin") {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if (!sender.isOp()) {
                    sender.sendMessage(Component.text("You are not an op"));
                    return false;
                }

                if (args.length == 0) {
                    sender.sendMessage(Component.text("No args! Modules: SyncChat, QQCheck"));
                    return false;
                }

                switch (args[0].toLowerCase()) {
                    case "syncchat":  {
                        Config.syncChatEnabled = !Config.syncChatEnabled;
                        sender.sendMessage(Component.text("SyncChat was " + Config.syncChatEnabled));
                        break;
                    }
                    case "qqcheck": {
                        Config.qqCheckEnabled = !Config.qqCheckEnabled;
                        sender.sendMessage(Component.text("QQCheck was " + Config.qqCheckEnabled));
                        break;
                    }
                    default: {
                        sender.sendMessage(Component.text("Unknown module: " + args[0]));
                        sender.sendMessage(Component.text("Modules: SyncChat, QQCheck"));
                        return false;
                    }
                }

                configManager.save();
                return true;
            }
        });
    }

    @Override
    public void onDisable() {
        moduleManager.onDisable();
    }
}
