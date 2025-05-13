package fun.bm.command.main.executor.extra.sub.data.query;

import fun.bm.command.Command;
import fun.bm.command.main.executor.extra.sub.data.Query;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.data.DataProcessor.Data.DataStringBuilder.buildDataString;
import static fun.bm.util.MainEnv.LOGGER;
import static fun.bm.util.helper.CommandHelper.operatorCheck;

public class DataQueryByName extends Command.ExecutorE {
    public DataQueryByName() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (operatorCheck(sender)) {
            return true;
        }
        String Name;
        try {
            Name = args[0];
        } catch (Exception e) {
            LOGGER.info(String.valueOf(e));
            return true;
        }
        String playerJson = Query.dataGet.getPlayersByNameAsJson(Name);
        return buildDataString(sender, playerJson);
    }
}
