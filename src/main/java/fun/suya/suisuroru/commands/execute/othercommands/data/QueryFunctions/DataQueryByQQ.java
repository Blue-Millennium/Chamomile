package fun.suya.suisuroru.commands.execute.othercommands.data.QueryFunctions;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fun.suya.suisuroru.data.AuthData.DataGet;
import fun.suya.suisuroru.data.AuthData.DataProcess;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;

import static fun.xd.suka.Main.LOGGER;

public class DataQueryByQQ implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("您没有权限这么做");
            return false;
        }
        DataGet dataGet = new DataGet();
        long QQNum = 0;
        try {
            QQNum = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§c输入的数据不是数字");
            return true;
        } catch (Exception e) {
            LOGGER.info(String.valueOf(e));
            return false;
        }
        String playerJson = dataGet.getPlayersByQQAsJson(QQNum);
        if (!playerJson.isEmpty() && !playerJson.equals("[]")) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Object>>() {}.getType();
            List<Object> playerList = gson.fromJson(playerJson, listType);
            for (Object player : playerList) {
                String processedData = DataProcess.processData(gson.toJson(player));
                sender.sendMessage(processedData);
            }
            return true;
        } else {
            sender.sendMessage("查询的数据不存在");
            return false;
        }
    }
}
