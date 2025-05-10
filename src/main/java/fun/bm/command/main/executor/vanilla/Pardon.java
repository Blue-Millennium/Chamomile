package fun.bm.command.main.executor.vanilla;

import fun.bm.command.Command;
import fun.bm.config.Config;
import fun.bm.data.DataManager.UnionBan.UnionBanData;
import fun.bm.module.impl.UnionBan;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.command.main.executor.vanilla.Ban.BanMessage;
import static fun.bm.data.DataManager.UnionBan.Local.OnlineDataMerge.reportBanData;

/**
 * @author Suisuroru
 * Date: 2024/10/27 14:30
 * function: Add some function to the vanilla pardon command
 */
public class Pardon extends Command.ExecutorV {

    public Pardon() {
        super("pardon");
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (Config.UnionBanEnabled) {
            if (!sender.isOp()) {
                sender.sendMessage("您没有权限这么做");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("Usage: /pardon <player>");
                return true;
            }

            String playerName = args[0];

            if (!Bukkit.getBanList(BanList.Type.PROFILE).isBanned(playerName)) {
                sender.sendMessage("玩家 " + playerName + " 未被封禁");
                return true;
            }

            // 调用原版的 pardon 命令
            boolean result = vanillaExecutor(sender, args);

            if (result) {
                // 额外操作---to UnionBan
                transferToUnionPardon(playerName, sender);
            }

            return result;
        } else {
            return vanillaExecutor(sender, args);
        }
    }

    private void transferToUnionPardon(String playerName, CommandSender sender) {
        String message = "玩家 " + playerName + " 已被 " + sender.getName() + "解除封禁";
        BanMessage("Local", message);
        if (!Config.UnionBanCheckOnly) {
            for (UnionBanData data : UnionBan.dataList) {
                if (data.playerName.equals(playerName) || data.playerUuid.toString().equals(playerName)) {
                    reportBanData(data.playerName, data.playerUuid, System.currentTimeMillis(), "Pardon", Config.ServerName);
                    return;
                }
            }
        }
    }
}
