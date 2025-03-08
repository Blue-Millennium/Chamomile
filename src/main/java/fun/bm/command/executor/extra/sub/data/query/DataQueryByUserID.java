package fun.bm.command.executor.extra.sub.data.query;

import fun.bm.command.manager.model.ExecutorE;
import fun.bm.data.AuthData.DataGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.command.executor.extra.sub.data.query.DataQueryByQQ.LongProcess;
import static fun.bm.data.AuthData.DataProcess.ProcessFinalData;
import static fun.bm.util.CommandOperatorCheck.checkNotOperator;

public class DataQueryByUserID extends ExecutorE {
    public DataQueryByUserID() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (checkNotOperator(sender)) {
            return true;
        }
        DataGet dataGet = new DataGet();
        long UserID = LongProcess(sender, args);
        if (UserID == 0) {
            return true;
        }
        String playerJson = dataGet.getPlayersByUserIDAsJson(UserID);
        return ProcessFinalData(sender, playerJson);
    }
}
