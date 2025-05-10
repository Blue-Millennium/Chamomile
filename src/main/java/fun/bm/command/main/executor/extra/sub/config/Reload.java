package fun.bm.command.main.executor.extra.sub.config;

import fun.bm.command.Command;
import fun.bm.util.MainEnv;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.util.MainEnv.LOGGER;
import static fun.bm.util.helper.CommandHelper.operatorCheck;

/**
 * @author Suisuroru
 * Date: 2024/9/28 13:24
 * function: Reload config
 */
public class Reload extends Command.ExecutorE {

    public Reload() {
        super("cmreload");
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        // 检查发送者是否具有OP权限
        if (operatorCheck(sender)) {
            return true;
        }

        try {
            // 调用 load 方法加载配置
            MainEnv.configManager.load();
            MainEnv.moduleManager.setupModules(false);
            sender.sendMessage("重新加载配置文件成功！");
        } catch (Exception e) {
            sender.sendMessage("重新加载配置文件失败！");
            LOGGER.warning(e.getMessage());
        }
        return true;
    }
}
