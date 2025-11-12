package fun.bm.chamomile.command.modules.extra.sub.check;

import fun.bm.chamomile.command.ExtraCommand;
import fun.bm.chamomile.data.basedata.BaseData;
import fun.bm.chamomile.util.Environment;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.chamomile.util.data.link.LinkDataStringBuilder.buildLinkDataString;

public class Delete extends ExtraCommand implements CommandExecutor {
    public Delete() {
        super(null);
    }

    public static boolean linkDataProcess(String name, String timestamp) {
        BaseData data = Environment.dataManager.baseDataManager.getPlayerDataByName(name);
        if (data == null || data.linkData == null) {
            return false;
        }
        data.linkData.removeIf(linkData -> linkData.linkedTime == Long.parseLong(timestamp));
        Environment.dataManager.baseDataManager.setPlayerData(data.playerData.playerUuid, data, true);
        return true;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || !args[0].matches("\\d+")) {
            sender.sendMessage(buildLinkDataString(sender.getName()));
            sender.sendMessage("§4请输入时间戳");
        } else {
            if (linkDataProcess(sender.getName(), args[0])) {
                sender.sendMessage("§c尝试删除成功");
            } else {
                sender.sendMessage("§4删除失败");
            }
        }
        return true;
    }
}
