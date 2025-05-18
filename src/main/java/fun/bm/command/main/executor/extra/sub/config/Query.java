package fun.bm.command.main.executor.extra.sub.config;

import fun.bm.command.Command;
import fun.bm.config.Config;
import fun.bm.util.MainEnv;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

import static fun.bm.util.MainEnv.LOGGER;
import static fun.bm.util.helper.CommandHelper.operatorCheck;

/**
 * @author Suisuroru
 * Date: 2024/10/15 00:43
 * function: Query config
 */
public class Query extends Command.ExecutorE {
    public Query() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        // 检查发送者是否具有OP权限
        if (operatorCheck(sender)) {
            return true;
        }
        if (args.length == 0) {
            if (command.getName().equals("cm")) {
                sender.sendMessage("§c/cm config query [查询配置]");
            } else if (command.getName().equals("chamomile")) {
                sender.sendMessage("§c/chamomile config query [查询配置]");
            }
            String allConfigNames = String.join("|", MainEnv.configManager.getConfigFieldNames());
            sender.sendMessage("§a所有配置项名称: " + allConfigNames);
        } else {
            String configName = args[0];
            if (!MainEnv.configManager.getConfigFieldNames().contains(configName)) {
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
