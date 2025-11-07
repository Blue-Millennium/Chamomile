package fun.bm.chamomile.data.manager.data;

import com.google.gson.stream.JsonReader;
import fun.bm.chamomile.data.manager.data.link.LinkData;
import fun.bm.chamomile.data.manager.data.link.QQLinkData;
import fun.bm.chamomile.data.manager.data.link.UseridLinkData;
import fun.bm.chamomile.data.manager.data.player.PlayerData;
import fun.bm.chamomile.util.GsonUtil;
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
            com.google.gson.JsonArray dataArray = GsonUtil.createGson().fromJson(jsonReader, com.google.gson.JsonArray.class);

            DATA_LIST = new ArrayList<>();

            if (dataArray != null) {
                for (int i = 0; i < dataArray.size(); i++) {
                    com.google.gson.JsonElement dataElement = dataArray.get(i);
                    Data data = GsonUtil.createGson().fromJson(dataElement, Data.class);

                    // fix linkData
                    if (data.linkData != null && dataElement.isJsonObject()) {
                        com.google.gson.JsonObject dataObject = dataElement.getAsJsonObject();
                        if (dataObject.has("linkData") && dataObject.get("linkData").isJsonArray()) {
                            com.google.gson.JsonArray linkDataArray = dataObject.getAsJsonArray("linkData");
                            List<LinkData> fixedList = new ArrayList<>();

                            for (int j = 0; j < data.linkData.size(); j++) {
                                LinkData originalLinkData = data.linkData.get(j);
                                if (j < linkDataArray.size() && linkDataArray.get(j).isJsonObject()) {
                                    com.google.gson.JsonObject linkDataObject = linkDataArray.get(j).getAsJsonObject();
                                    LinkData fixedLinkData = restoreLinkDataTypeFromJson(originalLinkData, linkDataObject);
                                    fixedList.add(fixedLinkData);
                                } else {
                                    fixedList.add(originalLinkData);
                                }
                            }
                            data.linkData = fixedList;
                        }
                    }

                    DATA_LIST.add(data);
                }
            }

            // file is empty
            if (DATA_LIST == null) {
                DATA_LIST = new ArrayList<>();
            }
            save(true);
        } catch (Exception exception) {
            LOGGER.warning("Failed to read data file " + exception.getMessage());
            save(true);
        }
    }


    private LinkData restoreLinkDataTypeFromJson(LinkData linkData, com.google.gson.JsonObject linkDataObject) {
        try {
            // 根据 JSON 中的独有键判断类型
            if (linkDataObject.has("qqNumber")) {
                long qqNumber = linkDataObject.get("qqNumber").getAsLong();
                long linkedTime = linkDataObject.has("linkedTime") ? linkDataObject.get("linkedTime").getAsLong() : 0L;
                if (qqNumber != 0) {
                    return new QQLinkData(qqNumber, linkedTime);
                }
            }

            if (linkDataObject.has("userid")) {
                long userid = linkDataObject.get("userid").getAsLong();
                long linkedGroup = linkDataObject.has("useridLinkedGroup") ? linkDataObject.get("useridLinkedGroup").getAsLong() :
                        linkDataObject.has("linkedGroup") ? linkDataObject.get("linkedGroup").getAsLong() : 0L;
                long linkedTime = linkDataObject.has("useridLinkedTime") ? linkDataObject.get("useridLinkedTime").getAsLong() :
                        linkDataObject.has("linkedTime") ? linkDataObject.get("linkedTime").getAsLong() : 0L;
                if (userid != 0) {
                    return new UseridLinkData(userid, linkedGroup, linkedTime);
                }
            }
        } catch (Exception e) {
            LOGGER.warning("Failed to restore LinkData type from JSON: " + e.getMessage());
        }

        return linkData;
    }


    public void save(boolean refactor) {
        try {
            if (refactor) refactorData();
            FileWriter fileWriter = new FileWriter(dataFile);
            fileWriter.write(GsonUtil.createGson().toJson(DATA_LIST));
            fileWriter.close();
        } catch (Exception e) {
            LOGGER.warning("Failed to save data file " + e.getMessage());
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

    public void setPlayerData(UUID uuid, Data data, boolean save) {
        Data data_old = getPlayerData(uuid);
        if (data_old != null) DATA_LIST.remove(data_old);
        DATA_LIST.add(data);
        if (save) save(true);
    }

    public void setPlayerDataByName(String name, Data data, boolean save) {
        Data data_old = getPlayerDataByName(name);
        if (data_old != null) DATA_LIST.remove(getPlayerDataByName(name));
        DATA_LIST.add(data);
        if (save) save(true);
    }

    public void refactorData() {
        List<Data> tempList = new ArrayList<>();
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
            tempList.add(data);
        }
        for (Data data : tempList) {
            setPlayerData(data.playerData.playerUuid, data, false);
        }
        save(false);
    }

    private boolean hasField(Object obj, String fieldName) {
        try {
            obj.getClass().getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private <T> T getFieldValue(Object obj, String fieldName, T defaultValue) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private LinkData restoreLinkDataType(LinkData linkData) {
        try {
            // qq
            if (hasField(linkData, "qqNumber")) {
                long qqNumber = getFieldValue(linkData, "qqNumber", 0L);
                long linkedTime = getFieldValue(linkData, "linkedTime", 0L);
                if (qqNumber != 0) {
                    return new QQLinkData(qqNumber, linkedTime);
                }
            }

            // Userid
            if (hasField(linkData, "userid") || hasField(linkData, "useridLinkedGroup")) {
                long userid = getFieldValue(linkData, "userid", 0L);
                long linkedGroup = getFieldValue(linkData, "linkedGroup", 0L);
                long linkedTime = getFieldValue(linkData, "linkedTime", 0L);
                if (userid != 0) {
                    return new UseridLinkData(userid, linkedGroup, linkedTime);
                }
            }
        } catch (Exception e) {
            LOGGER.warning("Failed to restore LinkData type: " + e.getMessage());
        }

        return linkData;
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
