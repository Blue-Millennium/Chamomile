package fun.blue_millennium.commands.execute.vanilla;

import fun.blue_millennium.Chamomile;
import fun.blue_millennium.config.Config;
import fun.blue_millennium.message.WebhookForEmail;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static fun.blue_millennium.data.UnionBan.LocalProcess.ReportedDataProcess.reportBanData;

/**
 * @author Suisuroru
 * Date: 2024/10/27 14:18
 * function: Add some function to the vanilla ban command
 */
public class Ban implements CommandExecutor {

    public static void BanMessage(String origin, String message) {
        Bukkit.broadcastMessage(origin + " Ban : " + message);
        if (Config.QQRobotEnabled & !Config.BotModeOfficial) {
            try {
                Chamomile.BOT.getGroup(Config.SyncChatGroup).sendMessage(message);
                Chamomile.BOT.getGroup(Config.ReportGroup).sendMessage(message);
            } catch (Exception e) {
                Chamomile.LOGGER.info("Error when report message to QQ group");
            }
            try {
                WebhookForEmail webhook = new WebhookForEmail();
                webhook.formatAndSendWebhook(origin + " Ban : " + message, message, Config.WebHookEmail);
            } catch (Exception e) {
                Chamomile.LOGGER.info("Error when report message to Email");
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (Config.UnionBanEnabled) {
            if (!sender.isOp()) {
                sender.sendMessage("您没有权限这么做");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("Usage: /ban <player> [reason]");
                return true;
            }

            String playerName = args[0];
            String reason = args.length > 1 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "No reason provided";
            Player targetPlayer = Bukkit.getPlayer(playerName);

            if (targetPlayer == null) {
                sender.sendMessage("未找到玩家: " + playerName);
                return true;
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
        BanMessage("Local", message);
        if (!Config.UnionBanCheckOnly) {
            reportBanData(targetPlayer.getName(), targetPlayer.getUniqueId(), System.currentTimeMillis(), reason, Config.ServerName);
        }
    }
}
