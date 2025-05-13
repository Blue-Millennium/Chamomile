package fun.bm.command.main.executor.extra.sub.data.query;

import fun.bm.command.Command;
import fun.bm.command.main.executor.extra.sub.data.Query;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static fun.bm.data.DataProcessor.Data.DataStringBuilder.buildDataString;
import static fun.bm.util.MainEnv.LOGGER;
import static fun.bm.util.helper.CommandHelper.operatorCheck;

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
        String playerJson = Query.dataGet.getPlayersByUUIDAsJson(Uuid);
        return buildDataString(sender, playerJson);
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
