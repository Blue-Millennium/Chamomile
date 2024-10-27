package fun.suya.suisuroru.commands.execute.othercommands.vanilla;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Suisuroru
 * Date: 2024/10/27 14:30
 * function: Add some function to the vanilla pardon command
 */
public class Pardon implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("您没有权限这么做");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /pardon <player>");
            return false;
        }

        String playerName = args[0];

        if (!Bukkit.getBanList(BanList.Type.PROFILE).isBanned(playerName)) {
            sender.sendMessage("玩家 " + playerName + " 未被封禁");
            return false;
        }

        // 调用原版的 pardon 命令
        boolean result = Bukkit.dispatchCommand(sender, "minecraft:pardon " + playerName);

        if (result) {
            // 额外操作---to UnionBan
            TransferToUnionPardon(playerName, sender);
        }

        return result;
    }

    private void TransferToUnionPardon(String playerName, CommandSender sender) {
        String message = "玩家 " + playerName + " 已被 " + sender.getName() + "解除封禁";
        Bukkit.broadcastMessage("本地黑名单: " + message);
    }
}
