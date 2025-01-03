package fun.suya.suisuroru.data.AuthData;

public class PlayerRecord {
    private final PlayerData playerData;
    private final long firstJoin;
    private final long lastJoin;
    private final long qqNumber;
    private final long linkedTime;
    private final String firstJoinIp;
    private final String lastJoinIp;

    // Constructor
    public PlayerRecord(PlayerData playerData, long firstJoin, long lastJoin, long qqNumber, long linkedTime, String firstJoinIp, String lastJoinIp) {
        this.playerData = playerData;
        this.firstJoin = firstJoin;
        this.lastJoin = lastJoin;
        this.qqNumber = qqNumber;
        this.linkedTime = linkedTime;
        this.firstJoinIp = firstJoinIp;
        this.lastJoinIp = lastJoinIp;
    }

    // Getters
    public PlayerData getPlayerData() {
        return playerData;
    }

    public long getFirstJoin() {
        return firstJoin;
    }

    public long getLastJoin() {
        return lastJoin;
    }

    public long getQqNumber() {
        return qqNumber;
    }

    public long getLinkedTime() {
        return linkedTime;
    }

    public String getFirstJoinIp() {
        return firstJoinIp;
    }

    public String getLastJoinIp() {
        return lastJoinIp;
    }

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
