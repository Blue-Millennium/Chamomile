package fun.blue_millennium.commands.execute.othercommands.sub.data.query;

import fun.blue_millennium.data.AuthData.DataGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.blue_millennium.Main.LOGGER;
import static fun.blue_millennium.commands.execute.othercommands.sub.data.query.DataQueryByQQ.LongProcess;
import static fun.blue_millennium.data.AuthData.DataProcess.ProcessFinalData;
import static fun.blue_millennium.util.CommandOperatorCheck.checkNotOperator;

public class DataQueryByUserID implements CommandExecutor {
    @Override
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
