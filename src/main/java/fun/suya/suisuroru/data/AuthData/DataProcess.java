package fun.suya.suisuroru.data.AuthData;

import com.google.gson.Gson;

public class DataProcess {

    public static String processData(String jsonData) {
        Gson gson = new Gson();
        AuthData authData = gson.fromJson(jsonData, AuthData.class);
        StringBuilder result = new StringBuilder();
        result.append("玩家名称: ").append(authData.getPlayerData().getPlayerName()).append("\n");
        result.append("玩家UUID: ").append(authData.getPlayerData().getPlayerUuid()).append("\n");
        result.append("首次加入时间: ").append(authData.getFirstJoin()).append("\n");
        result.append("最后加入时间: ").append(authData.getLastJoin()).append("\n");
        result.append("QQ号码: ").append(authData.getQqNumber()).append("\n");
        result.append("绑定时间: ").append(authData.getLinkedTime()).append("\n");
        result.append("首次加入IP: ").append(authData.getFirstJoinIp()).append("\n");
        result.append("最后加入IP: ").append(authData.getLastJoinIp()).append("\n");

        return result.toString();
    }
}
