package fun.bm.command.executor.extra.sub.config;

import fun.bm.command.manager.model.ExecutorE;
import fun.bm.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.Chamomile.LOGGER;
import static fun.bm.config.ConfigManager.getConfigFieldNames;
import static fun.bm.util.CommandOperatorCheck.checkNotOperator;

/**
 * @author Suisuroru
 * Date: 2024/10/15 02:06
 * function: Set new config
 */
public class Set extends ExecutorE {
    ConfigManager configManager = new ConfigManager();

    public Set() {
        super(null);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 检查发送者是否具有OP权限
        if (checkNotOperator(sender)) {
            return true;
        }

        if (args.length == 1) {
            if (command.getName().equals("cm")) {
                sender.sendMessage("§c/cm config set [修改参数] [修改值]");
            } else if (command.getName().equals("chamomile")) {
                sender.sendMessage("§c/chamomile config set [修改参数] [修改值]");
            }
            String allConfigNames = String.join("|", getConfigFieldNames());
            sender.sendMessage("§a所有配置项名称: " + allConfigNames);
            return true;
        }

        String configName = args[0];
        String value = args[1];

        // 检查配置项是否存在
        if (!getConfigFieldNames().contains(configName)) {
            sender.sendMessage("§c配置项 " + configName + " 不存在，请检查拼写");
            return true;
        }

        try {
            configManager.setConfigValue(configName, value);
            sender.sendMessage("§a配置项 " + configName + " 已成功设置为 " + value);
        } catch (Exception e) {
            sender.sendMessage("§c修改配置项 " + configName + " 的值时出错，请检查配置文件或联系开发人员。");
            LOGGER.warning(e.getMessage());
            return true;
        }

        return true;
    }

}
