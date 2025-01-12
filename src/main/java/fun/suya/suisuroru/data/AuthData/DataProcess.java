package fun.suya.suisuroru.data.AuthData;

import com.google.gson.Gson;

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
}
