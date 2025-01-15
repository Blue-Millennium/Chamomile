package fun.suya.suisuroru.data.AuthData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fun.xd.suka.Main.DATA_FILE;
import static fun.xd.suka.Main.LOGGER;

public class DataGet {
    private final List<PlayerRecord> playerRecords;
    private final Gson gson = new Gson();  // 创建 Gson 实例

    public DataGet() {
        this.playerRecords = readDataFromFile(DATA_FILE);
    }

    private List<PlayerRecord> readDataFromFile(File file) {
        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<PlayerRecord>>() {
            }.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<PlayerRecord> getPlayersByQQ(long qqNumber) {
        return playerRecords.stream()
                .filter(record -> record.qqNumber() == qqNumber)
                .toList();
    }

    public List<PlayerRecord> getPlayersByName(String playerName) {
        return playerRecords.stream()
                .filter(record -> record.playerData().playerName().equals(playerName))
                .toList();
    }

    public List<PlayerRecord> getPlayersByUUID(UUID playerUuid) {
        return playerRecords.stream()
                .filter(record -> {
                    UUID recordUuid = record.playerData().playerUuid();
                    return recordUuid.equals(playerUuid);
                })
                .toList();
    }

    public String getPlayersByQQAsJson(long qqNumber) {
        List<PlayerRecord> players = getPlayersByQQ(qqNumber);
        if (players.size() == 1) {
            return gson.toJson(players.get(0));
        }
        return gson.toJson(players);
    }

    public String getPlayersByNameAsJson(String playerName) {
        List<PlayerRecord> players = getPlayersByName(playerName);
        if (players.size() == 1) {
            return gson.toJson(players.get(0));
        }
        return gson.toJson(players);
    }

    public String getPlayersByUUIDAsJson(UUID playerUuid) {
        List<PlayerRecord> players = getPlayersByUUID(playerUuid);
        if (players.size() == 1) {
            return gson.toJson(players.get(0));
        }
        return gson.toJson(players);
    }

}
