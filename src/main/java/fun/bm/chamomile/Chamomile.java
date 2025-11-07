package fun.bm.chamomile;

import fun.bm.chamomile.util.Environment;
import fun.bm.chamomile.util.helper.MainThreadHelper;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Liycxc
 * Rewritted by Suisuroru
 * Date: 2024/7/16 下午8:38
 */
public class Chamomile extends JavaPlugin {

    public void onLoad() {
        MainThreadHelper.setupBaseEnv(this);
    }


    public void onEnable() {
        // 启动逻辑 - Start
        MainThreadHelper.bootQQBot();
        Environment.commandManager.registerCommands();
        Environment.functionManager.onEnable();
        // 启动逻辑 - End
        MainThreadHelper.sendStartMessage(true);
    }


    public void onDisable() {
        // 关闭逻辑 - Start
        Environment.functionManager.onDisable();
        // 关闭逻辑 - End
        MainThreadHelper.sendStartMessage(false);
    }
}
