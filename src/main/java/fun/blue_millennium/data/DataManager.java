package fun.blue_millennium.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import fun.blue_millennium.Chamomile;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.UUID;

import static fun.blue_millennium.Chamomile.LOGGER;

/**
 * @author Liycxc
 * Date: 2024/7/17 下午5:58
 */
public class DataManager {
    public ArrayList<Data> DATA_LIST = new ArrayList<>();

    public void load() {
        try (JsonReader jsonReader = new JsonReader(new FileReader(Chamomile.DATA_FILE))) {
            DATA_LIST = new Gson().fromJson(jsonReader, new TypeToken<ArrayList<Data>>() {
            }.getType());

            jsonReader.close();

            // file is empty
            if (DATA_LIST == null) {
                DATA_LIST = new ArrayList<>();
            }
        } catch (Exception exception) {
            LOGGER.warning("Failed to read data file " + exception.getMessage());
            save();
        }
    }

    public void save() {
        try {
            FileWriter fileWriter = new FileWriter(Chamomile.DATA_FILE);
            fileWriter.write(new Gson().toJson(DATA_LIST));
            fileWriter.close();
        } catch (Exception exception) {
            LOGGER.warning("Failed to save data file" + exception.getMessage());
        }
    }

    public Data getPlayerData(UUID uuid) {
        if (DATA_LIST != null) {
            for (Data data : DATA_LIST) {
                if (data.playerData.playerUuid.equals(uuid)) {
                    return data;
                }
            }
        }
        return null;
    }

    public Data getPlayerDataByName(String name) {
        if (DATA_LIST != null) {
            for (Data data : DATA_LIST) {
                if (data.playerData.playerName.equals(name)) {
                    return data;
                }
            }
        }
        return null;
    }

    public void setPlayerData(UUID uuid, Data data) {
        DATA_LIST.remove(getPlayerData(uuid));
        DATA_LIST.add(data);
        save();
    }

    public void setPlayerDataByName(String name, Data data) {
        DATA_LIST.remove(getPlayerDataByName(name));
        DATA_LIST.add(data);
        save();
    }
}
