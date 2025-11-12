package fun.bm.chamomile.command.modules.executor.extra.sub.check.bind;

import fun.bm.chamomile.command.ExtraCommand;
import fun.bm.chamomile.data.basedata.BaseData;
import fun.bm.chamomile.util.Environment;
import fun.bm.chamomile.util.TimeUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.chamomile.util.Environment.LOGGER;

public class BindUserid extends ExtraCommand implements CommandExecutor {
    public BindUserid() {
        super(null);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        BaseData data = Environment.dataManager.baseDataManager.getPlayerDataByName(args[0]);
        if (data == null) {
            LOGGER.warning("§4Some errors occur in data processing.");
            return true;
        } else {
            data.userid = Long.parseLong(args[2]);
            data.useridLinkedTime = TimeUtil.getUnixTimeMs();
            data.useridLinkedGroup = Long.parseLong(args[3]);
            data.useridChecked = true;
            Environment.dataManager.baseDataManager.setPlayerDataByName(args[0], data, true);
            sender.sendMessage("§e已将游戏名为" + args[0] + "的玩家绑定QQ为" + args[1]);
        }
        return true;
    }
}
