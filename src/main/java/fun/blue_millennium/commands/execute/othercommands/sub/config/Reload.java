package fun.blue_millennium.commands.execute.othercommands.sub.config;

import fun.blue_millennium.config.ConfigManager;
import fun.blue_millennium.module.ModuleManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.blue_millennium.Main.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/9/28 13:24
 * function: Reload config
 */
public class Reload implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 检查发送者是否具有OP权限
        if (!sender.isOp()) {
            sender.sendMessage("你没有权限执行此命令！");
            return true;
        }

        try {
            ConfigManager manager = new ConfigManager();
            // 调用 load 方法加载配置
            manager.load();
            ModuleManager mgr = new ModuleManager();
            mgr.reload();
            sender.sendMessage("重新加载配置文件成功！");
            return true;
        } catch (Exception e) {
            sender.sendMessage("重新加载配置文件失败！");
            LOGGER.warning(e.getMessage());
            return true;
        }
    }
}
