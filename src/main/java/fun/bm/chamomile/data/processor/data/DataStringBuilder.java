package fun.bm.chamomile.data.processor.data;

import fun.bm.chamomile.data.manager.data.Data;
import fun.bm.chamomile.data.manager.data.player.OldName;
import fun.bm.chamomile.data.processor.data.link.LinkDataStringBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static fun.bm.chamomile.util.Environment.LOGGER;

public class DataStringBuilder {

    public static String buildPlayerDataString(Data data) {
        StringBuilder result = new StringBuilder();
        appendPlayerData(result, data);
        appendIfNotNull(result, "首次加入时间: ", transformTime(data.firstJoin));
        appendIfNotNull(result, "首次加入时间(原始): ", data.firstJoin);
        appendIfNotNull(result, "最后加入时间: ", transformTime(data.lastJoin));
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
        appendIfNotNull(result, "QQ绑定标志: ", transformBoolean(data.qqChecked));
        appendIfNotNull(result, "UserID绑定标志: ", transformBoolean(data.useridChecked));
        if (data.linkData != null) {
            appendIfNotNull(result, LinkDataStringBuilder.buildLinkDataString(data));
        }
    }

    private static void appendPlayerData(StringBuilder result, Data data) {
        if (data.playerData != null) {
            appendIfNotNull(result, "玩家名称: ", data.playerData.playerName);
            appendIfNotNull(result, "玩家UUID: ", data.playerData.playerUuid);
            if (data.playerData.oldNames != null) {
                try {
                    appendOldNameData(result, data.playerData.oldNames);
                } catch (Exception e) {
                    LOGGER.warning("Error when transform Old Name Data.");
                }
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
                appendIfNotNull(result, "玩家名称 " + i + " 服务器内被替换时间: ", transformTime(oldName.updateTime));
                appendIfNotNull(result, "玩家名称 " + i++ + " 服务器内被替换时间(原始): ", oldName.updateTime);
            }
        } else {
            LOGGER.warning("Old name data is Null.");
        }
    }

    public static String transformBoolean(Boolean value) {
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
            LOGGER.warning("Error when transform Boolean");
            return null;
        }
    }

    public static boolean buildDataString(@NotNull CommandSender sender, List<Data> data) {
        if (!data.isEmpty()) {
            StringBuilder result = new StringBuilder();
            result.append("\n");
            appendIfNotNull(result, "§a-------------------");
            if (data.size() > 1) {
                appendIfNotNull(result, "§a查询到多个玩家数据，共 " + data.size() + " 个");
                appendIfNotNull(result, "§a-------------------");
            }
            int Count = 1;
            for (Data player : data) {
                if (data.size() > 1) {
                    appendIfNotNull(result, "§a第 " + Count++ + " 个玩家数据");
                }
                String processedData = DataStringBuilder.buildPlayerDataString(player);
                appendIfNotNull(result, "§a", processedData);
                appendIfNotNull(result, "§a-------------------");
            }
            sender.sendMessage(result.toString());
        } else {
            sender.sendMessage("查询的数据不存在");
        }
        return true;
    }

    public static String transformTime(long timestamp) {
        try {
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            String transformedTime = sdf.format(date);
            return transformedTime + "(GMT+8)";
        } catch (Exception e) {
            LOGGER.warning("Error when transform time");
            return null;
        }
    }

}
