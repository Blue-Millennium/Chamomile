package fun.bm.command.main.executor.extra.sub.data.query;

import fun.bm.command.Command;
import fun.bm.data.DataProcessor.Data.DataGet;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.command.main.executor.extra.sub.data.query.DataQueryByQQ.longProcess;
import static fun.bm.data.DataProcessor.Data.DataStringBuilder.buildDataString;
import static fun.bm.util.helper.CommandHelper.operatorCheck;

public class DataQueryByUserID extends Command.ExecutorE {
    public DataQueryByUserID() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (operatorCheck(sender)) {
            return true;
        }
        DataGet dataGet = new DataGet();
        long UserID = longProcess(sender, args);
        if (UserID == 0) {
            return true;
        }
        String playerJson = dataGet.getPlayersByUserIDAsJson(UserID);
        return buildDataString(sender, playerJson);
    }
}
