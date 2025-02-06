package fun.blue_millennium.commands.execute.othercommands.sub.config;

import fun.blue_millennium.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

import static fun.blue_millennium.Main.LOGGER;
import static fun.blue_millennium.config.ConfigManager.getConfigFieldNames;

/**
 * @author Suisuroru
 * Date: 2024/10/15 00:43
 * function: Query config
 */
public class Query implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 检查发送者是否具有OP权限
        if (!sender.isOp()) {
            sender.sendMessage("你没有权限执行此命令！");
            return true;
        }
        if (args.length == 0) {
            if (command.getName().equals("cm")) {
                sender.sendMessage("§c/cm config query [查询配置]");
            } else if (command.getName().equals("chamomile")) {
                sender.sendMessage("§c/chamomile config query [查询配置]");
            }
            String allConfigNames = String.join("|", getConfigFieldNames());
            sender.sendMessage("§a所有配置项名称: " + allConfigNames);
        } else {
            String configName = args[0];
            if (!getConfigFieldNames().contains(configName)) {
                sender.sendMessage("§c配置项 " + configName + " 不存在，请检查拼写");
                return true;
            }
            try {
                Field field = Config.class.getDeclaredField(configName);
                Object value = field.get(null);
                sender.sendMessage("§a配置项 " + configName + " 当前的值为: " + value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                sender.sendMessage("§c无法获取配置项 " + configName + " 的值，请检查配置文件或联系开发人员。");
                LOGGER.warning(e.getMessage());
            }
        }
        return true;
    }
}
