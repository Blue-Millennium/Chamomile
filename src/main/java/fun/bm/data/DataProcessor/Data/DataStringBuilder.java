package fun.bm.data.DataProcessor.Data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fun.bm.data.DataManager.LoginData.Data;
import fun.bm.data.DataManager.LoginData.LinkData.LinkData;
import fun.bm.data.DataManager.LoginData.LinkData.QQLinkData;
import fun.bm.data.DataManager.LoginData.LinkData.UseridLinkData;
import fun.bm.data.DataManager.LoginData.PlayerData.OldName;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static fun.bm.util.MainEnv.LOGGER;

public class DataStringBuilder {

    public static String buildPlayerDataString(String jsonData) {
        Gson gson = new Gson();
        Data data = gson.fromJson(jsonData, Data.class);
        StringBuilder result = new StringBuilder();
        appendPlayerData(result, data);
        appendIfNotNull(result, "首次加入时间: ", transferTime(data.firstJoin));
        appendIfNotNull(result, "首次加入时间(原始): ", data.firstJoin);
        appendIfNotNull(result, "最后加入时间: ", transferTime(data.lastJoin));
        appendIfNotNull(result, "最后加入时间(原始): ", data.lastJoin);
        appendLinkData(result, data);
        appendIfNotNull(result, "首次加入IP: ", data.firstJoinIp);
        appendIfNotNull(result, "最后加入IP: ", data.lastJoinIp);

        return result.toString();
    }

    public static void appendIfNotNull(StringBuilder result, String label, Object value) {
        if (value != null) {
            result.append(label).append(value).append("\n");
        }
    }

    public static void appendIfNotNull(StringBuilder result, Object value) {
        if (value != null) {
            result.append(value).append("\n");
        }
    }

    private static void appendLinkData(StringBuilder result, Data data) {
        appendIfNotNull(result, "QQ绑定标志: ", transferBoolean(data.qqChecked));
        appendIfNotNull(result, "UserID绑定标志: ", transferBoolean(data.useridChecked));
        if (data.linkData != null) {
            appendIfNotNull(result, "§a-------------------");
            if (data.linkData.size() > 1) {
                appendIfNotNull(result, "§a查询到多个绑定数据，共 " + data.linkData.size() + " 个");
                appendIfNotNull(result, "§a-------------------");
            }
            int i = 1;
            for (LinkData linkData : data.linkData) {
                appendIfNotNull(result, "§a查询到第 " + i++ + " 个绑定数据");
                try {
                    if (linkData instanceof QQLinkData qqlinkData) {
                        appendQQData(result, qqlinkData);
                    } else if (linkData instanceof UseridLinkData useridLinkData) {
                        appendUseridData(result, useridLinkData);
                    }
                    appendIfNotNull(result, "§a-------------------");
                } catch (Exception e) {
                    LOGGER.warning(e.getMessage());
                }
            }
        }
    }

    private static void appendQQData(StringBuilder result, QQLinkData data) {
        appendIfNotNull(result, "QQ号码: ", data.qqNumber);
        appendIfNotNull(result, "QQ绑定时间: ", transferTime(data.linkedTime));
        appendIfNotNull(result, "QQ绑定时间戳: ", data.linkedTime);
    }

    private static void appendUseridData(StringBuilder result, UseridLinkData data) {
        appendIfNotNull(result, "UserID识别码: ", data.userid);
        appendIfNotNull(result, "UserID绑定的群聊: ", data.useridLinkedGroup);
        appendIfNotNull(result, "UserID绑定时间: ", transferTime(data.linkedTime));
        appendIfNotNull(result, "UserID绑定时间戳: ", data.linkedTime);
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
            appendIfNotNull(result, "旧的玩家名称: 存在，共 " + oldNamesList.size() + " 个");
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

    public static String transferBoolean(Boolean value) {
        try {
            if (value == null) {
                return "未知";
            }
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

    public static boolean buildDataString(@NotNull CommandSender sender, String playerJson) {
        if (!playerJson.isEmpty() && !playerJson.equals("[]")) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Object>>() {
            }.getType();
            List<Object> playerList = gson.fromJson(playerJson, listType);
            StringBuilder result = new StringBuilder();
            result.append("\n");
            appendIfNotNull(result, "§a-------------------");
            if (playerList.size() > 1) {
                appendIfNotNull(result, "§a查询到多个玩家数据，共 " + playerList.size() + " 个");
                appendIfNotNull(result, "§a-------------------");
            }
            int Count = 1;
            for (Object player : playerList) {
                if (playerList.size() > 1) {
                    appendIfNotNull(result, "§a第 " + Count++ + " 个玩家数据");
                }
                String processedData = DataStringBuilder.buildPlayerDataString(gson.toJson(player));
                appendIfNotNull(result, "§a", processedData);
                appendIfNotNull(result, "§a-------------------");
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
