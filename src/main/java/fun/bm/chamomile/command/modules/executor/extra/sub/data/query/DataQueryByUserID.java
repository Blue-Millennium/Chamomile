package fun.bm.chamomile.command.modules.executor.extra.sub.data.query;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.command.modules.executor.extra.sub.data.Query;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.chamomile.command.modules.executor.extra.sub.data.query.DataQueryByQQ.longProcess;
import static fun.bm.chamomile.data.processor.data.DataStringBuilder.buildDataString;
import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

public class DataQueryByUserID extends Command.ExecutorE {
    public DataQueryByUserID() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (operatorCheck(sender)) {
            return true;
        }
        long UserID = longProcess(sender, args);
        if (UserID == 0) {
            return true;
        }
        String playerJson = Query.dataGet.getPlayersByUserIDAsJson(UserID);
        return buildDataString(sender, playerJson);
    }
}
