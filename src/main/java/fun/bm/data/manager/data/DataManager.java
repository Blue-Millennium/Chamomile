package fun.bm.data.manager.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import fun.bm.data.manager.data.link.LinkData;
import fun.bm.data.manager.data.link.QQLinkData;
import fun.bm.data.manager.data.link.UseridLinkData;
import fun.bm.util.MainEnv;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fun.bm.util.MainEnv.LOGGER;

/**
 * @author Liycxc
 * Date: 2024/7/17 下午5:58
 */
public class DataManager {
    public ArrayList<Data> DATA_LIST = new ArrayList<>();

    public void load() {
        try (JsonReader jsonReader = new JsonReader(new FileReader(MainEnv.DATA_FILE))) {
            DATA_LIST = new Gson().fromJson(jsonReader, new TypeToken<ArrayList<Data>>() {
            }.getType());

            jsonReader.close();

            // file is empty
            if (DATA_LIST == null) {
                DATA_LIST = new ArrayList<>();
            }
            save();
        } catch (Exception exception) {
            LOGGER.warning("Failed to read data file " + exception.getMessage());
            save();
        }
    }

    public void save() {
        try {
            refactorData();
            FileWriter fileWriter = new FileWriter(MainEnv.DATA_FILE);
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
        Data data_old = getPlayerData(uuid);
        if (data_old != null) DATA_LIST.remove(data_old);
        DATA_LIST.add(data);
        save();
    }

    public void setPlayerDataByName(String name, Data data) {
        Data data_old = getPlayerDataByName(name);
        if (data_old != null) DATA_LIST.remove(getPlayerDataByName(name));
        DATA_LIST.add(data);
        save();
    }

    public void refactorData() {
        for (Data data : DATA_LIST) {
            // Process unofficial data
            if (data.qqNumber != 0) {
                try {
                    List<LinkData> linkDataList = data.linkData == null ? new ArrayList<>() : data.linkData;
                    QQLinkData linkData = new QQLinkData();
                    linkData.qqNumber = data.qqNumber;
                    linkData.linkedTime = data.linkedTime;
                    data.qqNumber = 0;
                    data.linkedTime = 0;
                    linkDataList.add(linkData);
                    data.linkData = linkDataList;
                    setPlayerData(data.playerData.playerUuid, data);
                } catch (Exception e) {
                    LOGGER.warning(e.getMessage());
                }
            }

            // Process official data
            if (data.userid != 0) {
                if (data.useridLinkedGroup == 0) {
                    data.userid = 0;
                    data.useridLinkedTime = 0;
                } else {
                    try {
                        List<LinkData> linkDataList = data.linkData == null ? new ArrayList<>() : data.linkData;
                        UseridLinkData linkData = new UseridLinkData();
                        linkData.userid = data.userid;
                        linkData.useridLinkedGroup = data.useridLinkedGroup;
                        linkData.linkedTime = data.useridLinkedTime;
                        data.userid = 0;
                        data.useridLinkedGroup = 0;
                        data.useridLinkedTime = 0;
                        linkDataList.add(linkData);
                        data.linkData = linkDataList;
                        setPlayerData(data.playerData.playerUuid, data);
                    } catch (Exception e) {
                        LOGGER.warning(e.getMessage());
                    }
                }
            }
        }
    }
}
