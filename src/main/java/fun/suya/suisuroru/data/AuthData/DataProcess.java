package fun.suya.suisuroru.data.AuthData;

import com.google.gson.Gson;

public class DataProcess {

    public static String processData(String jsonData) {
        Gson gson = new Gson();
        AuthData authData = gson.fromJson(jsonData, AuthData.class);
        StringBuilder result = new StringBuilder();
        result.append("玩家名称: ").append(authData.playerData().playerName()).append("\n");
        result.append("玩家UUID: ").append(authData.playerData().playerUuid()).append("\n");
        result.append("首次加入时间: ").append(authData.firstJoin()).append("\n");
        result.append("最后加入时间: ").append(authData.lastJoin()).append("\n");
        result.append("QQ号码: ").append(authData.qqNumber()).append("\n");
        result.append("绑定时间: ").append(authData.linkedTime()).append("\n");
        result.append("首次加入IP: ").append(authData.firstJoinIp()).append("\n");
        result.append("最后加入IP: ").append(authData.lastJoinIp()).append("\n");

        return result.toString();
    }
}
