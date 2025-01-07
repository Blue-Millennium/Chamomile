package fun.suya.suisuroru.commands.execute.othercommands.data.QueryFunctions;

import fun.suya.suisuroru.data.AuthData.DataGet;
import fun.suya.suisuroru.data.AuthData.DataProcess;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static fun.xd.suka.Main.LOGGER;

public class DataQueryByUUID implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("您没有权限这么做");
            return false;
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
            return false;
        } catch (Exception e) {
            LOGGER.info(String.valueOf(e));
            return false;
        }
        String playerJson = dataGet.getPlayersByUUIDAsJson(Uuid);
        if (!playerJson.isEmpty() && !playerJson.equals("[]")) {
            sender.sendMessage(DataProcess.processData(playerJson));
            return true;
        } else {
            sender.sendMessage("查询的数据不存在");
            return false;
        }
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
