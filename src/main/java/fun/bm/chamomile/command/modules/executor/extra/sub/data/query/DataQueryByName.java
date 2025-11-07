package fun.bm.chamomile.command.modules.executor.extra.sub.data.query;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.command.modules.executor.extra.sub.data.Query;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.chamomile.data.processor.data.DataStringBuilder.buildDataString;
import static fun.bm.chamomile.util.Environment.LOGGER;
import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

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
        return buildDataString(sender, Query.dataGet.getPlayersByName(Name));
    }
}
