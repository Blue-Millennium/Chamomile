package fun.bm.command.modules.executor.extra.sub.check.bind;

import fun.bm.command.Command;
import fun.bm.data.manager.data.Data;
import fun.bm.util.MainEnv;
import fun.bm.util.TimeUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.util.MainEnv.LOGGER;

public class BindUserid extends Command.ExecutorE {
    public BindUserid() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        Data data = MainEnv.dataManager.getPlayerDataByName(args[0]);
        if (data == null) {
            LOGGER.warning("§4Some errors occur in data processing.");
            return true;
        } else {
            data.userid = Long.parseLong(args[2]);
            data.useridLinkedTime = TimeUtil.getUnixTimeMs();
            data.useridLinkedGroup = Long.parseLong(args[3]);
            data.useridChecked = true;
            MainEnv.dataManager.setPlayerDataByName(args[0], data);
            MainEnv.dataManager.save();
            sender.sendMessage("§e已将游戏名为" + args[0] + "的玩家绑定QQ为" + args[1]);
        }
        return true;
    }
}
