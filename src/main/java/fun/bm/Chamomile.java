package fun.bm;

import fun.bm.command.CommandManager;
import fun.bm.util.helper.EmailSender;
import fun.bm.util.MainEnv;
import fun.bm.util.helper.MainThreadHelper;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Liycxc
 * Date: 2024/7/16 下午8:38
 */
public class Chamomile extends JavaPlugin implements Listener {
    @Override
    public void onLoad() {
        MainThreadHelper.SetupBaseEnv(this);
    }

    @Override
    public void onEnable() {
        // 启动逻辑 - Start
        MainThreadHelper.Boot_QQBot();
        CommandManager.registerCommands();
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
