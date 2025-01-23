package fun.suya.suisuroru.data.AuthData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DataProcess {

    public static String processData(String jsonData) {
        Gson gson = new Gson();
        PlayerRecord playerRecord = gson.fromJson(jsonData, PlayerRecord.class);
        StringBuilder result = new StringBuilder();

        appendIfNotNull(result, "玩家名称: ", playerRecord.playerData().playerName());
        appendIfNotNull(result, "玩家UUID: ", playerRecord.playerData().playerUuid());
        appendIfNotNull(result, "首次加入时间: ", transferTime(playerRecord.firstJoin()));
        appendIfNotNull(result, "首次加入时间(原始): ", playerRecord.firstJoin());
        appendIfNotNull(result, "最后加入时间: ", transferTime(playerRecord.lastJoin()));
        appendIfNotNull(result, "最后加入时间(原始): ", playerRecord.lastJoin());
        appendIfNotNull(result, "QQ号码: ", playerRecord.qqNumber());
        appendIfNotNull(result, "绑定时间: ", playerRecord.linkedTime());
        appendIfNotNull(result, "首次加入IP: ", playerRecord.firstJoinIp());
        appendIfNotNull(result, "最后加入IP: ", playerRecord.lastJoinIp());

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
            StringBuilder result = new StringBuilder();
            result.append("\n");
            appendIfNotNull(result, "§a", "-------------------");
            appendIfNotNull(result, "§a", "查询到" + playerList.size() + "个玩家数据");
            appendIfNotNull(result, "§a", "-------------------");
            for (Object player : playerList) {
                appendIfNotNull(result, "§a", "第" + Count++ + "个玩家数据");
                String processedData = DataProcess.processData(gson.toJson(player));
                appendIfNotNull(result, "§a", processedData);
                appendIfNotNull(result, "§a", "-------------------");
            }
            sender.sendMessage(result.toString());
        } else {
            sender.sendMessage("查询的数据不存在");
        }
        return true;
    }

    private static String transferTime(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String transferredTime = sdf.format(date);
        return transferredTime + "(GMT+8)";
    }

}
