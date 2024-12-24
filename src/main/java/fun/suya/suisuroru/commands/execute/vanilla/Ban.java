package fun.suya.suisuroru.commands.execute.vanilla;

import fun.suya.suisuroru.config.Config;
import fun.suya.suisuroru.module.impl.UnionBan;
import fun.xd.suka.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;

import static fun.suya.suisuroru.module.impl.UnionBan.reportBanData;

/**
 * @author Suisuroru
 * Date: 2024/10/27 14:18
 * function: Add some function to the vanilla ban command
 */
public class Ban implements CommandExecutor {

    public static void BanMessage(String message) {
        Bukkit.broadcastMessage("本地黑名单: " + message);
        try {
            if (Config.QQRobotEnabled) {
                Main.BOT.getGroup(Config.syncChatGroup).sendMessage(message);
                Main.BOT.getGroup(Config.reportGroup).sendMessage(message);
            }
        } catch (Exception e) {
            Main.LOGGER.info("Error when report message to QQ group");
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
                sender.sendMessage("Usage: /ban <player> [reason]");
                return false;
            }

            String playerName = args[0];
            String reason = args.length > 1 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "No reason provided";
            Player targetPlayer = Bukkit.getPlayer(playerName);

            if (targetPlayer == null) {
                sender.sendMessage("未找到玩家: " + playerName);
                return false;
            }

            // 调用原版的 ban 命令
            boolean result = Bukkit.dispatchCommand(sender, "minecraft:ban " + playerName + " " + reason);

            if (result) {
                // 额外操作---to UnionBan
                TransferToUnionBan(targetPlayer, sender, reason);
            }

            return result;
        } else {
            String reason = args.length > 1 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "";
            return Bukkit.dispatchCommand(sender, "minecraft:ban " + args[0] + " " + reason);
        }
    }

    private void TransferToUnionBan(Player targetPlayer, CommandSender sender, String reason) {
        String message = "玩家 " + targetPlayer.getName() + " 已被 " + sender.getName() + " 以[ " + reason + " ]的理由封禁";
        BanMessage(message);
        if (!Config.UnionBanCheckOnly) {
            reportBanData(new UnionBan.BanPair<>(targetPlayer.getUniqueId(), reason, new Date(), Config.servername));
        }
    }
}
