package fun.bm.chamomile.command.modules.executor.extra.sub.config;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.util.MainEnv;
import fun.bm.chamomile.util.helper.MainThreadHelper;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.chamomile.util.MainEnv.LOGGER;
import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

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
        if (operatorCheck(sender)) {
            return true;
        }

        try {
            MainEnv.functionManager.preReload();
            MainThreadHelper.bootQQBot();
            MainEnv.configManager.reload();
            MainEnv.functionManager.reload();
            sender.sendMessage("重新加载配置文件成功！");
        } catch (Exception e) {
            sender.sendMessage("重新加载配置文件失败！");
            LOGGER.warning(e.getMessage());
        }
        return true;
    }
}
