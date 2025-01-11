package fun.suya.suisuroru.data.AuthData;

public record AuthData(PlayerData playerData, long firstJoin, long lastJoin, long qqNumber, long linkedTime,
                       String firstJoinIp, String lastJoinIp) {
}
