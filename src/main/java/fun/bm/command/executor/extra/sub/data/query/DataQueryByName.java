package fun.bm.command.executor.extra.sub.data.query;

import fun.bm.command.Command;
import fun.bm.data.AuthData.DataGet;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.Chamomile.LOGGER;
import static fun.bm.data.AuthData.DataProcess.ProcessFinalData;
import static fun.bm.util.helper.OperatorChecker.checkNotOperator;

public class DataQueryByName extends Command.ExecutorE {
    public DataQueryByName() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (checkNotOperator(sender)) {
            return true;
        }
        DataGet dataGet = new DataGet();
        String Name;
        try {
            Name = args[0];
        } catch (Exception e) {
            LOGGER.info(String.valueOf(e));
            return true;
        }
        String playerJson = dataGet.getPlayersByNameAsJson(Name);
        return ProcessFinalData(sender, playerJson);
    }
}
