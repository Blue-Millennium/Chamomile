package fun.bm.command.execute.extra.sub.data.query;

import fun.bm.command.execute.Executor;
import fun.bm.data.AuthData.DataGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.command.execute.extra.sub.data.query.DataQueryByQQ.LongProcess;
import static fun.bm.data.AuthData.DataProcess.ProcessFinalData;
import static fun.bm.util.CommandOperatorCheck.checkNotOperator;

public class DataQueryByUserID extends Executor {
    public DataQueryByUserID() {
        super(null);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
