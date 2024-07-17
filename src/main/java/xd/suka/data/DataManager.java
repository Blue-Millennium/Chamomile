package xd.suka.data;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

import static xd.suka.Main.logger;

/**
 * @author Liycxc
 * Date: 2024/7/17 下午5:58
 */
public class DataManager {
    public File DATA_DIR = new File("BaseData");
    public File DATA_FILE = new File(DATA_DIR, "data.json");
    public ArrayList<Data> dataList = new ArrayList<>();

    public void install() {
        try {
            DATA_DIR.mkdirs();
            if (!DATA_FILE.exists()) {
                DATA_FILE.createNewFile();
            }
        } catch (Exception exception) {
            logger.error("Failed to create data file", exception);
        }

        try {
            JsonReader jsonReader = new JsonReader(new FileReader(DATA_FILE));
            dataList = new Gson().fromJson(jsonReader, ArrayList.class);
            jsonReader.close();
            // file is empty
            if (dataList == null) {
                dataList = new ArrayList<>();
            }
        } catch (Exception exception) {
            logger.error("Failed to read data file", exception);
        }
    }

    public void saveAll() {
        try {
            FileWriter fileWriter = new FileWriter(DATA_FILE);
            fileWriter.write(new Gson().toJson(dataList));
            fileWriter.close();
        } catch (Exception exception) {
            logger.error("Failed to save data file", exception);
        }
    }

    public Data getPlayerData(UUID uuid) {
        if (dataList != null) {
            for (Data data : dataList) {
                if (data.PLAYER_DATA.PLAYER_UUID.equals(uuid)) {
                    return data;
                }
            }
        }
        return null;
    }

    public void setPlayerData(UUID uuid, Data data) {
        dataList.remove(getPlayerData(uuid));
        dataList.add(data);
        saveAll();
    }
}
