package fun.bm.data.AuthData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fun.bm.data.PlayerData.Data;
import fun.bm.data.PlayerData.OldName;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static fun.bm.util.MainEnv.LOGGER;

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
            appendIfNotNull(result, "QQ绑定时间: ", transferTime(data.linkedTime));
            appendIfNotNull(result, "QQ绑定时间(原始): ", data.linkedTime);
        } else {
            appendIfNotNull(result, "QQ绑定标志: ", transferBoolean(data.qqChecked));
            if (data.qqChecked) {
                appendIfNotNull(result, "QQ绑定时间: ", transferTime(data.linkedTime));
                appendIfNotNull(result, "QQ绑定时间(原始): ", data.linkedTime);
            }
        }
    }

    private static void appendAPPIDData(StringBuilder result, Data data) {
        boolean flag = false;
        if (data.useridChecked == null) {
            appendIfNotNull(result, "UserID绑定标志: ", "未知");
            flag = true;
        } else {
            appendIfNotNull(result, "UserID绑定标志: ", transferBoolean(data.useridChecked));
            if (data.useridChecked) flag = true;
        }
        if (flag) {
            appendIfNotNull(result, "UserID识别码: ", data.useridChecked);
            appendIfNotNull(result, "UserID绑定的群聊: ", data.useridLinkedGroup);
            appendIfNotNull(result, "UserID绑定时间: ", transferTime(data.useridLinkedTime));
            appendIfNotNull(result, "UserID绑定时间(原始): ", data.useridLinkedTime);
        }
    }

    private static void appendPlayerData(StringBuilder result, Data data) {
        if (data.playerData != null) {
            appendIfNotNull(result, "玩家名称: ", data.playerData.playerName);
            appendIfNotNull(result, "玩家UUID: ", data.playerData.playerUuid);
            try {
                appendOldNameData(result, data.playerData.oldNames);
            } catch (Exception e) {
                LOGGER.warning("Error when transfer Old Name Data or Data not found.");
            }
        } else {
            LOGGER.warning("Player data is Null.");
        }
    }

    private static void appendOldNameData(StringBuilder result, List<OldName> oldNamesList) {
        if (!oldNamesList.isEmpty()) {
            appendIfNotNull(result, "旧的玩家名称: ", "存在，共 " + oldNamesList.size() + " 个");
            int i = 1;
            for (OldName oldName : oldNamesList) {
                appendIfNotNull(result, "玩家名称 " + i + " : ", oldName.oldName);
                appendIfNotNull(result, "玩家名称 " + i + " 服务器内被替换时间: ", transferTime(oldName.updateTime));
                appendIfNotNull(result, "玩家名称 " + i++ + " 服务器内被替换时间(原始): ", oldName.updateTime);
            }
        } else {
            LOGGER.warning("Old name data is Null.");
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
            StringBuilder result = new StringBuilder();
            result.append("\n");
            appendIfNotNull(result, "§a", "-------------------");
            if (playerList.size() > 1) {
                appendIfNotNull(result, "§a", "查询到多个玩家数据，共 " + playerList.size() + " 个");
                appendIfNotNull(result, "§a", "-------------------");
            }
            int Count = 1;
            for (Object player : playerList) {
                if (playerList.size() > 1) {
                    appendIfNotNull(result, "§a", "第 " + Count++ + " 个玩家数据");
                }
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

    public static String transferTime(long timestamp) {
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
