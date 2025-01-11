package fun.suya.suisuroru.data.AuthData;

import java.util.UUID;

public record PlayerData(String playerName, UUID playerUuid) {

    @Override
    public String toString() {
        return "PlayerData{" +
                "playerName='" + playerName + '\'' +
                ", playerUuid=" + playerUuid +
                '}';
    }
}
