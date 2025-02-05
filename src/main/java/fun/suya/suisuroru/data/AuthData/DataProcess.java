package fun.suya.suisuroru.data.AuthData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fun.xd.suka.data.Data;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static fun.xd.suka.Main.LOGGER;

public class DataProcess {

    public static String processData(String jsonData) {
        Gson gson = new Gson();
        Data data = gson.fromJson(jsonData, Data.class);
        StringBuilder result = new StringBuilder();

        if (data != null) {
            appendIfNotNull(result, "玩家名称: ", data.playerData != null ? data.playerData.playerName : null);
            appendIfNotNull(result, "玩家UUID: ", data.playerData != null ? data.playerData.playerUuid : null);
            appendIfNotNull(result, "首次加入时间: ", data.firstJoin != -1 ? transferTime(data.firstJoin) : null);
            appendIfNotNull(result, "首次加入时间(原始): ", data.firstJoin);
            appendIfNotNull(result, "最后加入时间: ", data.lastJoin != -1 ? transferTime(data.lastJoin) : null);
            appendIfNotNull(result, "最后加入时间(原始): ", data.lastJoin);
            appendIfNotNull(result, "QQ绑定标志: ", transferBoolean(data.qqChecked));
            appendIfNotNull(result, "QQ号码: ", data.qqNumber);
            appendIfNotNull(result, "绑定时间: ", data.linkedTime);
            appendIfNotNull(result, "首次加入IP: ", data.firstJoinIp);
            appendIfNotNull(result, "最后加入IP: ", data.lastJoinIp);
        } else {
            LOGGER.warning("Data object is null");
        }

        return result.toString();
    }

    private static void appendIfNotNull(StringBuilder result, String label, Object value) {
        if (value != null) {
            result.append(label).append(value).append("\n");
        }
    }

    private static String transferBoolean(Boolean value) {
        if (value != null) {
            return value ? "是" : "否";
        } else {
            LOGGER.warning("Boolean value is null");
            return null;
        }
    }

    public static boolean ProcessFinalData(@NotNull CommandSender sender, String playerJson) {
        if (!playerJson.isEmpty() && !playerJson.equals("[]")) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Object>>() {}.getType();
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

    private static String transferTime(Long timestamp) {
        if (timestamp != null) {
            try {
                Date date = new Date(timestamp);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                String transferredTime = sdf.format(date);
                return transferredTime + "(GMT+8)";
            } catch (Exception e) {
                LOGGER.warning("Error when transfer time");
                return null;
            }
        } else {
            LOGGER.warning("Timestamp is null");
            return null;
        }
    }
}
