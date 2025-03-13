package fun.bm.command.main.executor.vanilla;

import fun.bm.command.Command;
import fun.bm.config.Config;
import fun.bm.util.helper.EmailSender;
import fun.bm.util.helper.MainEnv;
import net.mamoe.mirai.contact.Group;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static fun.bm.data.UnionBan.LocalProcessor.ReportedDataProcess.reportBanData;
import static fun.bm.module.impl.Reporter.ReportGroups;
import static fun.bm.module.impl.SyncChat.SyncGroups;
import static fun.bm.util.helper.MainEnv.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/10/27 14:18
 * function: Add some function to the vanilla ban command
 */
public class Ban extends Command.ExecutorV {

    public Ban() {
        super("ban");
    }

    public static void BanMessage(String origin, String message) {
        Bukkit.broadcastMessage(origin + " Ban : " + message);
        if (Config.QQRobotEnabled & !Config.BotModeOfficial) {
            try {
                for (long groupId : ReportGroups) {
                    Group reportGroup = MainEnv.BOT.getGroup(groupId);
                    reportGroup.sendMessage(message);
                }
                for (long groupId : SyncGroups) {
                    Group reportGroup = MainEnv.BOT.getGroup(groupId);
                    reportGroup.sendMessage(message);
                }
            } catch (Exception e) {
                LOGGER.info("Error when report message to QQ group");
            }
            try {
                EmailSender webhook = new EmailSender();
                webhook.formatAndSendWebhook(origin + " Ban : " + message, message, Config.WebHookEmail);
            } catch (Exception e) {
                LOGGER.info("Error when report message to Email");
            }
        }
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
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
            boolean result = vanillaCommand(sender, args);

            if (result) {
                // 额外操作---to UnionBan
                TransferToUnionBan(targetPlayer, sender, reason);
            }

            return result;
        } else {
            return vanillaCommand(sender, args);
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
