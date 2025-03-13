package fun.bm;

import fun.bm.command.CommandManager;
import fun.bm.config.ConfigManager;
import fun.bm.data.PlayerData.DataManager;
import fun.bm.module.ModuleManager;
import fun.bm.util.helper.EmailSender;
import fun.bm.util.helper.MainEnv;
import fun.bm.util.helper.MainThreadHelper;
import net.mamoe.mirai.utils.LoggerAdapters;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Liycxc
 * Date: 2024/7/16 下午8:38
 */
public class Chamomile extends JavaPlugin implements Listener {
    @Override
    public void onLoad() {
        if (MainEnv.INSTANCE == null) {
            MainEnv.INSTANCE = this;
            MainEnv.LOGGER = MainEnv.INSTANCE.getLogger();
        }

        MainEnv.configManager = new ConfigManager();
        MainEnv.dataManager = new DataManager();
        MainEnv.moduleManager = new ModuleManager();

        MainThreadHelper.SetupDirectories();

        MainEnv.dataManager.load();
        MainEnv.configManager.load();
        MainEnv.moduleManager.setupModules(true);
    }

    @Override
    public void onEnable() {
        // 启动逻辑 - Start
        Bukkit.getPluginManager().registerEvents(this, this); // 注册事件
        LoggerAdapters.useLog4j2(); // 使用 Log4j2 作为日志记录器
        MainThreadHelper.Boot_QQBot();
        CommandManager.registerCommand();
        MainEnv.moduleManager.onEnable();
        // 启动逻辑 - End
        EmailSender.CheckPlugin("启动");
    }

    @Override
    public void onDisable() {
        // 关闭逻辑 - Start
        MainEnv.moduleManager.onDisable();
        // 关闭逻辑 - End
        EmailSender.CheckPlugin("关闭");
    }
}
