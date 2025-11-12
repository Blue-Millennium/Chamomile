package fun.bm.chamomile.command.modules.executor.extra.sub.data.query;

import fun.bm.chamomile.command.ExtraCommand;
import fun.bm.chamomile.command.modules.executor.extra.sub.data.Query;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.chamomile.command.modules.executor.extra.sub.data.query.DataQueryByQQ.longProcess;
import static fun.bm.chamomile.util.data.DataStringBuilder.buildDataString;
import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

public class DataQueryByUserID extends ExtraCommand implements CommandExecutor {
    public DataQueryByUserID() {
        super(null);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (operatorCheck(sender)) {
            return true;
        }
        long UserID = longProcess(sender, args);
        if (UserID == 0) {
            return true;
        }
        return buildDataString(sender, Query.dataGet.getPlayersByUserID(UserID));
    }
}
