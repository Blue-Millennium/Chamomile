package fun.blue_millennium.data.UnionBan.LocalProcess;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fun.blue_millennium.Main.LOGGER;
import static fun.blue_millennium.Main.UNION_BAN_DATA_FILE;

public class UnionBanDataGet {
    private List<UnionBanData> dataList;

    public void load() {
        try (JsonReader jsonReader = new JsonReader(new FileReader(UNION_BAN_DATA_FILE))) {
            dataList = new Gson().fromJson(jsonReader, new TypeToken<ArrayList<UnionBanData>>() {
            }.getType());

            jsonReader.close();

            // file is empty
            if (dataList == null) {
                dataList = new ArrayList<>();
            }
        } catch (Exception exception) {
            LOGGER.warning("Failed to read UnionBan data file " + exception.getMessage());
            save();
        }
    }

    public void save() {
        try {
            FileWriter fileWriter = new FileWriter(UNION_BAN_DATA_FILE);
            fileWriter.write(new Gson().toJson(UNION_BAN_DATA_FILE));
            fileWriter.close();
        } catch (Exception exception) {
            LOGGER.warning("Failed to save UnionBan data file" + exception.getMessage());
        }
    }

    public UnionBanData getUnionBanData(UUID uuid) {
        if (dataList != null) {
            for (UnionBanData data : dataList) {
                if (data.playerUuid.equals(uuid)) {
                    return data;
                }
            }
        }
        return null;
    }

    public void setPlayerData(UUID uuid, UnionBanData data) {
        dataList.remove(getUnionBanData(uuid));
        dataList.add(data);
        save();
    }

    public List<UnionBanData> returnAllData() {
        return dataList;
    }
}
