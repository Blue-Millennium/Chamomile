package fun.bm.chamomile.command.modules.extra.sub.data.query;

import fun.bm.chamomile.command.ExtraCommand;
import fun.bm.chamomile.util.Environment;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.chamomile.util.Environment.LOGGER;
import static fun.bm.chamomile.util.data.DataStringBuilder.buildDataString;
import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

public class DataQueryByName extends ExtraCommand implements CommandExecutor {
    public DataQueryByName() {
        super(null);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
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
        return buildDataString(sender, Environment.dataManager.baseDataManager.getPlayersByName(Name));
    }
}
