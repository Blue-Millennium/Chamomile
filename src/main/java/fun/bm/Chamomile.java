package fun.bm;

import fun.bm.util.MainEnv;
import fun.bm.util.helper.EmailSender;
import fun.bm.util.helper.MainThreadHelper;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Liycxc
 * Date: 2024/7/16 下午8:38
 */
public class Chamomile extends JavaPlugin {
    @Override
    public void onLoad() {
        MainThreadHelper.SetupBaseEnv(this);
    }

    @Override
    public void onEnable() {
        // 启动逻辑 - Start
        MainThreadHelper.Boot_QQBot();
        MainEnv.commandManager.registerCommands();
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
