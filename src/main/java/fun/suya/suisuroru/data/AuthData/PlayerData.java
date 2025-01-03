package fun.suya.suisuroru.data.AuthData;

import java.util.UUID;

public class PlayerData {
    private final String playerName;
    private final UUID playerUuid;

    // Constructor
    public PlayerData(String playerName, UUID playerUuid) {
        this.playerName = playerName;
        this.playerUuid = playerUuid;
    }

    // Getters
    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "playerName='" + playerName + '\'' +
                ", playerUuid=" + playerUuid +
                '}';
    }
}
