package fun.bm;

import fun.bm.util.MainEnv;
import fun.bm.util.helper.MainThreadHelper;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Liycxc
 * Date: 2024/7/16 下午8:38
 */
public class Chamomile extends JavaPlugin {

    public void onLoad() {
        MainThreadHelper.setupBaseEnv(this);
    }


    public void onEnable() {
        // 启动逻辑 - Start
        MainThreadHelper.bootQQBot();
        MainEnv.commandManager.registerCommands();
        MainEnv.moduleManager.onEnable();
        // 启动逻辑 - End
        MainEnv.emailSender.checkPlugin("启动");
    }


    public void onDisable() {
        // 关闭逻辑 - Start
        MainEnv.moduleManager.onDisable();
        // 关闭逻辑 - End
        MainEnv.emailSender.checkPlugin("关闭");
    }
}
