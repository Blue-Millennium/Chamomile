package fun.blue_millennium.command.execute.extra.sub.data.query;

import fun.blue_millennium.command.execute.Executor;
import fun.blue_millennium.data.AuthData.DataGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.blue_millennium.Chamomile.LOGGER;
import static fun.blue_millennium.data.AuthData.DataProcess.ProcessFinalData;
import static fun.blue_millennium.util.CommandOperatorCheck.checkNotOperator;

public class DataQueryByName extends Executor {
    public DataQueryByName() {
        super(null);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
