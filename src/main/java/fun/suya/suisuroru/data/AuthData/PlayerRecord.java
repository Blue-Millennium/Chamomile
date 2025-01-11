package fun.suya.suisuroru.data.AuthData;

public record PlayerRecord(PlayerData playerData, long firstJoin, long lastJoin, long qqNumber, long linkedTime,
                           String firstJoinIp, String lastJoinIp) {

    @Override
    public String toString() {
        return "PlayerRecord{" +
                "playerData=" + playerData +
                ", firstJoin=" + firstJoin +
                ", lastJoin=" + lastJoin +
                ", qqNumber=" + qqNumber +
                ", linkedTime=" + linkedTime +
                ", firstJoinIp='" + firstJoinIp + '\'' +
                ", lastJoinIp='" + lastJoinIp + '\'' +
                '}';
    }
}
