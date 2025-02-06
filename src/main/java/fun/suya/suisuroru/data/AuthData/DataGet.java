package fun.suya.suisuroru.data.AuthData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fun.xd.suka.data.Data;
import fun.xd.suka.data.PlayerData;

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
    private final List<Data> dataList;
    private final Gson gson = new Gson();  // 创建 Gson 实例

    public DataGet() {
        this.dataList = readDataFromFile(DATA_FILE);
    }

    private List<Data> readDataFromFile(File file) {
        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Data>>() {
            }.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<PlayerData> getPlayerDataByUserID(long userID) {
        return dataList.stream()
                .filter(record -> record.userid == userID)
                .map(record -> record.playerData)
                .toList();
    }


    public List<Data> getPlayersByQQ(long qqNumber) {
        return dataList.stream()
                .filter(record -> record.qqNumber == qqNumber)
                .toList();
    }

    public List<Data> getPlayersByName(String playerName) {
        return dataList.stream()
                .filter(record -> record.playerData.playerName.equals(playerName))
                .toList();
    }

    public List<Data> getPlayersByUUID(UUID playerUuid) {
        return dataList.stream()
                .filter(record -> {
                    UUID recordUuid = record.playerData.playerUuid;
                    return recordUuid.equals(playerUuid);
                })
                .toList();
    }

    public String getPlayersByQQAsJson(long qqNumber) {
        List<Data> players = getPlayersByQQ(qqNumber);
        return gson.toJson(players);
    }

    public String getPlayersByNameAsJson(String playerName) {
        List<Data> players = getPlayersByName(playerName);
        return gson.toJson(players);
    }

    public String getPlayersByUUIDAsJson(UUID playerUuid) {
        List<Data> players = getPlayersByUUID(playerUuid);
        return gson.toJson(players);
    }

}
