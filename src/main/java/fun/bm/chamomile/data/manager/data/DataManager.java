package fun.bm.chamomile.data.manager.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import fun.bm.chamomile.data.manager.data.link.LinkData;
import fun.bm.chamomile.data.manager.data.link.QQLinkData;
import fun.bm.chamomile.data.manager.data.link.UseridLinkData;
import fun.bm.chamomile.data.manager.data.player.PlayerData;
import fun.bm.chamomile.util.MainEnv;
import fun.bm.chamomile.util.helper.DirectoryAccessor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fun.bm.chamomile.util.MainEnv.LOGGER;

/**
 * @author Liycxc
 * Date: 2024/7/17 下午5:58
 */
public class DataManager {
    public final File dataFile = new File(MainEnv.BASE_DIR, "data.json");
    public ArrayList<Data> DATA_LIST = new ArrayList<>();

    public void load() {
        DirectoryAccessor.initFile(dataFile);
        try (JsonReader jsonReader = new JsonReader(new FileReader(dataFile))) {
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
            FileWriter fileWriter = new FileWriter(dataFile);
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
    }

    public void setPlayerDataByName(String name, Data data) {
        Data data_old = getPlayerDataByName(name);
        if (data_old != null) DATA_LIST.remove(getPlayerDataByName(name));
        DATA_LIST.add(data);
    }

    public void refactorData() {
        for (Data data : DATA_LIST) {
            nullCheck(data);

            // Init origin data
            List<LinkData> linkDataList = data.linkData == null ? new ArrayList<>() : data.linkData;

            // Process unofficial data
            if (data.qqNumber != 0) {
                try {
                    QQLinkData linkData = new QQLinkData(data.qqNumber, data.linkedTime);
                    data.qqNumber = data.linkedTime = 0;
                    data.qqChecked = true;
                    linkDataList.add(linkData);
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
                        UseridLinkData linkData = new UseridLinkData(data.userid, data.useridLinkedGroup, data.useridLinkedTime);
                        data.userid = data.useridLinkedGroup = data.useridLinkedTime = 0;
                        data.useridChecked = true;
                        linkDataList.add(linkData);
                    } catch (Exception e) {
                        LOGGER.warning(e.getMessage());
                    }
                }
            }

            // Check link data
            if (!linkDataList.isEmpty()) {
                data.linkData = linkDataList;
                for (LinkData linkData : linkDataList) {
                    try {
                        if (data.qqChecked && data.useridChecked) break;
                        if (linkData instanceof QQLinkData) data.qqChecked = true;
                        if (linkData instanceof UseridLinkData) data.useridChecked = true;
                    } catch (Exception e) {
                        LOGGER.warning(e.getMessage());
                    }
                }
            }
            setPlayerData(data.playerData.playerUuid, data);
        }
    }

    public Data nullCheck(Data data) {
        if (data == null) {
            data = new Data();
        }
        if (data.linkData == null) {
            data.linkData = new ArrayList<>();
        }
        if (data.playerData == null) {
            data.playerData = new PlayerData();
        }
        if (data.playerData.oldNames == null) {
            data.playerData.oldNames = new ArrayList<>();
        }
        return data;
    }
}
