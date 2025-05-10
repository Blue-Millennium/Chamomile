package fun.bm.command.main.executor.extra.sub.check;

import fun.bm.command.Command;
import fun.bm.data.DataManager.LoginData.Data;
import fun.bm.util.MainEnv;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.data.DataProcessor.LinkData.LinkDataStringBuilder.buildLinkDataString;

public class Delete extends Command.ExecutorE {
    public Delete() {
        super(null);
    }

    public static boolean linkDataProcess(String name, String timestamp) {
        Data data = MainEnv.dataManager.getPlayerDataByName(name);
        if (data == null || data.linkData == null) {
            return false;
        }
        data.linkData.removeIf(linkData -> linkData.linkedTime == Long.parseLong(timestamp));
        MainEnv.dataManager.setPlayerData(data.playerData.playerUuid, data);
        return true;
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || !args[0].matches("\\d+")) {
            buildLinkDataString(sender.getName());
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
