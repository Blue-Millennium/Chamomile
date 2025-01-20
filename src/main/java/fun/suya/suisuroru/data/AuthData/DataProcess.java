package fun.suya.suisuroru.data.AuthData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;

public class DataProcess {

    public static String processData(String jsonData) {
        Gson gson = new Gson();
        AuthData authData = gson.fromJson(jsonData, AuthData.class);
        StringBuilder result = new StringBuilder();

        appendIfNotNull(result, "玩家名称: ", authData.playerData().playerName());
        appendIfNotNull(result, "玩家UUID: ", authData.playerData().playerUuid());
        appendIfNotNull(result, "首次加入时间: ", authData.firstJoin());
        appendIfNotNull(result, "最后加入时间: ", authData.lastJoin());
        appendIfNotNull(result, "QQ号码: ", authData.qqNumber());
        appendIfNotNull(result, "绑定时间: ", authData.linkedTime());
        appendIfNotNull(result, "首次加入IP: ", authData.firstJoinIp());
        appendIfNotNull(result, "最后加入IP: ", authData.lastJoinIp());

        return result.toString();
    }

    private static void appendIfNotNull(StringBuilder result, String label, Object value) {
        if (value != null) {
            result.append(label).append(value).append("\n");
        }
    }

    public static boolean ProcessFinalData(@NotNull CommandSender sender, String playerJson) {
        if (!playerJson.isEmpty() && !playerJson.equals("[]")) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Object>>() {
            }.getType();
            List<Object> playerList = gson.fromJson(playerJson, listType);
            int Count = 1;
            sender.sendMessage("§a---------------");
            sender.sendMessage("§a查询到" + playerList.size() + "个玩家数据");
            sender.sendMessage("§a---------------");
            for (Object player : playerList) {
                sender.sendMessage("§a第" + Count++ + "个玩家数据");
                String processedData = DataProcess.processData(gson.toJson(player));
                sender.sendMessage(processedData);
                sender.sendMessage("§a---------------");
            }
        } else {
            sender.sendMessage("查询的数据不存在");
        }
        return true;
    }

}
