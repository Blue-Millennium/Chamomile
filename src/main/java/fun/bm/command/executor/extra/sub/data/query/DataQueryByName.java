package fun.bm.command.executor.extra.sub.data.query;

import fun.bm.command.manager.model.ExecutorE;
import fun.bm.data.AuthData.DataGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.Chamomile.LOGGER;
import static fun.bm.data.AuthData.DataProcess.ProcessFinalData;
import static fun.bm.util.CommandOperatorCheck.checkNotOperator;

public class DataQueryByName extends ExecutorE {
    public DataQueryByName() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
