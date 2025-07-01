package fun.bm.chamomile.data.processor.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fun.bm.chamomile.data.manager.data.Data;
import fun.bm.chamomile.data.manager.data.link.QQLinkData;
import fun.bm.chamomile.data.manager.data.link.UseridLinkData;
import fun.bm.chamomile.util.MainEnv;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fun.bm.chamomile.util.MainEnv.LOGGER;

public class DataGet {
    private final List<Data> dataList;
    private final Gson gson = new Gson();

    public DataGet() {
        this.dataList = readDataFromFile();
    }

    private List<Data> readDataFromFile() {
        try (FileReader reader = new FileReader(MainEnv.DATA_FILE)) {
            Type listType = new TypeToken<List<Data>>() {
            }.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            LOGGER.warning(e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Data> getPlayersByUserID(long userID) {
        try {
            return dataList.stream()
                    .filter(record -> record.linkData.stream()
                            .anyMatch(linkData -> linkData instanceof UseridLinkData && ((UseridLinkData) linkData).userid == userID))
                    .toList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Data> getPlayersByQQ(long qqNumber) {
        try {
            return dataList.stream()
                    .filter(record -> record.linkData.stream()
                            .anyMatch(linkData -> linkData instanceof QQLinkData && ((QQLinkData) linkData).qqNumber == qqNumber))
                    .toList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Data> getPlayersByName(String playerName) {
        try {
            return dataList.stream()
                    .filter(record -> record.playerData.playerName.equals(playerName))
                    .toList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Data> getPlayersByUUID(UUID playerUuid) {
        try {
            return dataList.stream()
                    .filter(record -> {
                        UUID recordUuid = record.playerData.playerUuid;
                        return recordUuid.equals(playerUuid);
                    })
                    .toList();
        } catch (Exception e) {
            return null;
        }
    }

    public String getPlayersByUserIDAsJson(long userID) {
        List<Data> players = getPlayersByUserID(userID);
        return gson.toJson(players);
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

    public String transformListDataToJson(List<Data> data) {
        return gson.toJson(data);
    }
}
