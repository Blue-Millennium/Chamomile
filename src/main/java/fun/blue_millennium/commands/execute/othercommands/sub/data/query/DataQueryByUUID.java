package fun.blue_millennium.commands.execute.othercommands.sub.data.query;

import fun.blue_millennium.data.AuthData.DataGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static fun.blue_millennium.Main.LOGGER;
import static fun.blue_millennium.data.AuthData.DataProcess.ProcessFinalData;
import static fun.blue_millennium.util.CommandOperatorCheck.checkNotOperator;

public class DataQueryByUUID implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (checkNotOperator(sender)) {
            return true;
        }
        DataGet dataGet = new DataGet();
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
        String playerJson = dataGet.getPlayersByUUIDAsJson(Uuid);
        return ProcessFinalData(sender, playerJson);
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
