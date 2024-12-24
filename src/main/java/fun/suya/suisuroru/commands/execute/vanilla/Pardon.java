package fun.suya.suisuroru.commands.execute.vanilla;

import fun.suya.suisuroru.config.Config;
import fun.suya.suisuroru.module.impl.UnionBan;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static fun.suya.suisuroru.commands.execute.vanilla.Ban.BanMessage;
import static fun.suya.suisuroru.module.impl.UnionBan.reportBanData;

/**
 * @author Suisuroru
 * Date: 2024/10/27 14:30
 * function: Add some function to the vanilla pardon command
 */
public class Pardon implements CommandExecutor {

    public static void TransferToUnionPardon(String playerName, CommandSender sender) {
        String message = "玩家 " + playerName + " 已被 " + sender.getName() + "解除封禁";
        BanMessage(message);
        if (!Config.UnionBanCheckOnly) {
            Player targetPlayer = Bukkit.getPlayer(playerName);
            String reason = "Pardon";
            if (targetPlayer != null) {
                reportBanData(new UnionBan.BanPair<>(targetPlayer.getUniqueId(), reason, new Date(), "Pardon"));
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (Config.UnionBanEnabled) {
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
        } else {
            return Bukkit.dispatchCommand(sender, "minecraft:pardon " + args[0]);
        }
    }
}
