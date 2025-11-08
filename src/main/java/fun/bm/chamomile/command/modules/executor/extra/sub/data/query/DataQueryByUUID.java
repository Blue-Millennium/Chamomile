package fun.bm.chamomile.command.modules.executor.extra.sub.data.query;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.command.modules.executor.extra.sub.data.Query;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static fun.bm.chamomile.util.Environment.LOGGER;
import static fun.bm.chamomile.util.data.DataStringBuilder.buildDataString;
import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

public class DataQueryByUUID extends Command.ExecutorE {
    public DataQueryByUUID() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (operatorCheck(sender)) {
            return true;
        }
        UUID Uuid;
        try {
            String uuidString = args[0].toLowerCase();
            if (!uuidString.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
                uuidString = insertHyphens(uuidString);
            }
            Uuid = UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            LOGGER.info("§c输入的数据不是UUID");
            return true;
        } catch (Exception e) {
            LOGGER.info(String.valueOf(e));
            return true;
        }
        return buildDataString(sender, Query.dataGet.getPlayersByUUID(Uuid));
    }

    private String insertHyphens(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        sb.insert(8, '-');
        sb.insert(13, '-');
        sb.insert(18, '-');
        sb.insert(23, '-');
        return sb.toString();
    }

}
