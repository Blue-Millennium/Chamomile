package fun.bm.chamomile.command.modules.executor.extra.sub.config;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.util.MainEnv;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.chamomile.util.MainEnv.LOGGER;
import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

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
        if (operatorCheck(sender)) {
            return true;
        }
        if (args.length == 0) {
            if (command.getName().equals("cm")) {
                sender.sendMessage("§c/cm config query [查询配置]");
            } else if (command.getName().equals("chamomile")) {
                sender.sendMessage("§c/chamomile config query [查询配置]");
            }
            String allConfigNames = String.join("|", MainEnv.configManager.getAllConfigPaths(""));
            sender.sendMessage("§a所有配置项名称: " + allConfigNames);
        } else {
            String configName = args[0];
            if (!MainEnv.configManager.getAllConfigPaths("").contains(configName)) {
                sender.sendMessage("§c配置项 " + configName + " 不存在，请检查拼写");
                return true;
            }
            try {
                String value = MainEnv.configManager.getConfig(configName);
                if (value == null) {
                    sender.sendMessage("§a配置项 " + configName + " 不存在");
                    return true;
                }
                sender.sendMessage("§a配置项 " + configName + " 当前的值为: " + value);
            } catch (Exception e) {
                sender.sendMessage("§c无法获取配置项 " + configName + " 的值，请检查配置文件或联系开发人员。");
                LOGGER.warning(e.getMessage());
            }
        }
        return true;
    }
}
