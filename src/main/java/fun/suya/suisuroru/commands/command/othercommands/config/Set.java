package fun.suya.suisuroru.commands.command.othercommands.config;

import fun.suya.suisuroru.config.ConfigKeys;
import fun.suya.suisuroru.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author Suisuroru
 * Date: 2024/10/15 02:06
 * function: Set new config
 */
public class Set implements CommandExecutor {
    private static final Map<String, ConfigKeys> CONFIG_KEYS_MAP = ConfigKeys.configKeysList;
    ConfigManager configManager = new ConfigManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 检查发送者是否具有OP权限
        if (!sender.isOp()) {
            sender.sendMessage("你没有权限执行此命令！");
            return true;
        }
        if (args.length == 1) {
            if (command.getName().equals("bp")) {
                sender.sendMessage("§c/bp config set [修改参数] [修改值]");
            } else if (command.getName().equals("baseplugin")) {
                sender.sendMessage("§c/baseplugin config set [修改参数] [修改值]");
            }
            String allConfigNames = String.join("|", CONFIG_KEYS_MAP.keySet());
            sender.sendMessage("§a所有配置项名称: " + allConfigNames);
            return true;
        }

        String configName = args[0];
        String value = args[1];

        // 检查配置项是否存在
        if (!CONFIG_KEYS_MAP.containsKey(configName)) {
            sender.sendMessage("§c配置项 " + configName + " 不存在，请检查拼写");
            return false;
        }

        ConfigKeys key = CONFIG_KEYS_MAP.get(configName);
        try {
            configManager.setConfigValue(key, value);
            configManager.save();
            sender.sendMessage("§a配置项 " + configName + " 已成功设置为 " + value);
        } catch (Exception e) {
            sender.sendMessage("§c修改配置项 " + configName + " 的值时出错，请检查配置文件或联系开发人员。");
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
