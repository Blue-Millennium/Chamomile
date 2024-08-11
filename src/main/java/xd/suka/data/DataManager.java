package xd.suka.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import xd.suka.Main;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.UUID;

import static xd.suka.Main.LOGGER;

/**
 * @author Liycxc
 * Date: 2024/7/17 下午5:58
 */
public class DataManager {
    public ArrayList<Data> DATA_LIST = new ArrayList<>();

    public void load() {
        try (JsonReader jsonReader = new JsonReader(new FileReader(Main.INSTANCE.DATA_FILE))) {
            DATA_LIST = new Gson().fromJson(jsonReader, new TypeToken<ArrayList<Data>>() {}.getType());

            jsonReader.close();

            // file is empty
            if (DATA_LIST == null) {
                DATA_LIST = new ArrayList<>();
            }
        } catch (Exception exception) {
            LOGGER.error("Failed to read data file", exception);
            save();
        }
    }

    public void save() {
        try {
            FileWriter fileWriter = new FileWriter(Main.INSTANCE.DATA_FILE);
            fileWriter.write(new Gson().toJson(DATA_LIST));
            fileWriter.close();
        } catch (Exception exception) {
            LOGGER.error("Failed to save data file", exception);
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

    public void setPlayerData(UUID uuid, Data data) {
        DATA_LIST.remove(getPlayerData(uuid));
        DATA_LIST.add(data);
        save();
    }
}
