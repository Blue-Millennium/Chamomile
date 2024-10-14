package fun.suya.suisuroru.commands.command.othercommands.config;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import fun.suya.suisuroru.config.ConfigManager;

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
            sender.sendMessage("重新加载配置文件成功！");
            return true;
        } catch (Exception e) {
            sender.sendMessage("重新加载配置文件失败！");
            e.printStackTrace();
            return false;
        }
    }
}
