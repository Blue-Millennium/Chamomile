package fun.bm.command.main.executor.extra.sub.config;

import fun.bm.command.Command;
import fun.bm.util.MainEnv;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.util.MainEnv.LOGGER;
import static fun.bm.util.helper.CommandHelper.operatorCheck;

/**
 * @author Suisuroru
 * Date: 2024/10/15 02:06
 * function: Set new config
 */
public class Set extends Command.ExecutorE {
    public Set() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        // 检查发送者是否具有OP权限
        if (operatorCheck(sender)) {
            return true;
        }

        if (args.length == 1) {
            if (command.getName().equals("cm")) {
                sender.sendMessage("§c/cm config set [修改参数] [修改值]");
            } else if (command.getName().equals("chamomile")) {
                sender.sendMessage("§c/chamomile config set [修改参数] [修改值]");
            }
            String allConfigNames = String.join("|", MainEnv.configManager.getConfigFieldNames());
            sender.sendMessage("§a所有配置项名称: " + allConfigNames);
        } else {
            String configName = args[0];
            String value = args[1];
            // 检查配置项是否存在
            if (!MainEnv.configManager.getConfigFieldNames().contains(configName)) {
                sender.sendMessage("§c配置项 " + configName + " 不存在，请检查拼写");
            }
            try {
                MainEnv.configManager.setConfigValue(configName, value);
                sender.sendMessage("§a配置项 " + configName + " 已成功设置为 " + value);
            } catch (Exception e) {
                sender.sendMessage("§c修改配置项 " + configName + " 的值时出错，请检查配置文件或联系开发人员。");
                LOGGER.warning(e.getMessage());
            }
        }
        return true;
    }

}
