package fun.suya.suisuroru.commands.execute.vanilla;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Kill implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("您没有权限这么做");
            return false;
        }
        if (args[0].startsWith("@e")) {
            if (args[1].isEmpty()) {
                Command.broadcastCommandMessage(sender, "§c拒绝执行清除全部实体，请指定具体实体类型");
                return true;
            } else if (args[0].contains("type=!")) {
                Command.broadcastCommandMessage(sender, "§c拒绝执行清除多类型全部实体，请指定具体实体类型");
                return true;
            } else {
                Command.broadcastCommandMessage(sender, "§c拒绝执行清除全部实体，请指定具体实体类型");
            }
        } else if (args[0].equals("@a")) {
            Command.broadcastCommandMessage(sender, "§c拒绝执行清除全部玩家，请选择其他具体实体类型");
            return true;
        } else if (args[0].equals("@r")) {
            Command.broadcastCommandMessage(sender, "§c拒绝执行清除随机玩家，请选择其他具体实体类型");
            return true;
        } else if (args[0].equals("items")) {
            return Bukkit.dispatchCommand(sender, "minecraft:kill @e[type=item]");
        } else {
            return Bukkit.dispatchCommand(sender, "minecraft:kill " + String.join(" ", args));
        }
        return false;
    }
}
