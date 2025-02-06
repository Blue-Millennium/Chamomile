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
        appendPlayerData(result, data);
        appendIfNotNull(result, "首次加入时间: ", transferTime(data.firstJoin));
        appendIfNotNull(result, "首次加入时间(原始): ", data.firstJoin);
        appendIfNotNull(result, "最后加入时间: ", transferTime(data.lastJoin));
        appendIfNotNull(result, "最后加入时间(原始): ", data.lastJoin);
        appendQQData(result, data);
        appendAPPIDData(result, data);
        appendIfNotNull(result, "首次加入IP: ", data.firstJoinIp);
        appendIfNotNull(result, "最后加入IP: ", data.lastJoinIp);

        return result.toString();
    }

    private static void appendIfNotNull(StringBuilder result, String label, Object value) {
        if (value != null) {
            result.append(label).append(value).append("\n");
        }
    }

    private static void appendQQData(StringBuilder result, Data data) {
        if (data.qqChecked == null) {
            appendIfNotNull(result, "QQ绑定标志: ", "未知");
            appendIfNotNull(result, "QQ号码: ", data.qqNumber);
            appendIfNotNull(result, "QQ绑定时间: ", data.linkedTime);
        } else {
            appendIfNotNull(result, "QQ绑定标志: ", transferBoolean(data.qqChecked));
            if (data.qqChecked) {
                appendIfNotNull(result, "QQ号码: ", data.qqNumber);
                appendIfNotNull(result, "QQ绑定时间: ", data.linkedTime);
            }
        }
    }

    private static void appendAPPIDData(StringBuilder result, Data data) {
        if (data.useridChecked == null) {
            appendIfNotNull(result, "UserID绑定标志: ", "未知");
            appendIfNotNull(result, "UserID识别码: ", data.useridChecked);
            appendIfNotNull(result, "UserID绑定时间: ", data.useridLinkedTime);
        } else {
            appendIfNotNull(result, "UserID绑定标志: ", transferBoolean(data.useridChecked));
            if (data.useridChecked) {
                appendIfNotNull(result, "UserID识别码: ", data.userid);
                appendIfNotNull(result, "UserID绑定时间: ", data.useridLinkedTime);
            }
        }
    }

    private static void appendPlayerData(StringBuilder result, Data data) {
        if (data.playerData != null) {
            appendIfNotNull(result, "玩家名称: ", data.playerData.playerName);
            appendIfNotNull(result, "玩家UUID: ", data.playerData.playerUuid);
        } else {
            LOGGER.warning("Player data is Null.");
        }
    }

    private static String transferBoolean(Boolean value) {
        try {
            if (value) {
                return "是";
            } else {
                return "否";
            }
        } catch (Exception e) {
            LOGGER.warning("Error when transfer Boolean");
            return null;
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
    }

}
