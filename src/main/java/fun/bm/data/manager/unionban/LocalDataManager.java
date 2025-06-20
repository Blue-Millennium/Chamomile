package fun.bm.data.manager.unionban;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import fun.bm.config.modules.ServerConfig;
import fun.bm.function.modules.UnionBan;
import fun.bm.util.MainEnv;
import fun.bm.util.TimeUtil;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.profile.PlayerProfile;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static fun.bm.function.modules.UnionBan.dataList;
import static fun.bm.util.MainEnv.LOGGER;

public class LocalDataManager {
    public void load() {
        try (JsonReader jsonReader = new JsonReader(new FileReader(MainEnv.UNION_BAN_DATA_FILE))) {
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
        importBanList();
    }

    public void save() {
        try {
            FileWriter fileWriter = new FileWriter(MainEnv.UNION_BAN_DATA_FILE);
            fileWriter.write(new Gson().toJson(dataList));
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
        UnionBanData data_old = getUnionBanData(uuid);
        if (data_old != null) dataList.remove(getUnionBanData(uuid));
        dataList.add(data);
        save();
    }

    public void importBanList() {
        try {
            ProfileBanList banlist = Bukkit.getBanList(BanList.Type.PROFILE);
            for (BanEntry<PlayerProfile> banEntry : banlist.getEntries()) {
                UnionBanData data = UnionBan.localBanDataManager.getUnionBanData(banEntry.getBanTarget().getUniqueId());
                if (data == null) {
                    data = new UnionBanData();
                    data.playerName = banEntry.getBanTarget().getName();
                    data.playerUuid = banEntry.getBanTarget().getUniqueId();
                    Date expiration = banEntry.getExpiration();
                    data.time = expiration != null ? expiration.getTime() : TimeUtil.getUnixTimeMs();
                    data.reason = banEntry.getReason();
                    data.sourceServer = ServerConfig.serverName;
                    data.localTag = true;
                    if (UnionBan.onlineBanDataManager.reportRemoteBanList(data)) {
                        data.reportTag = true;
                    }
                    UnionBan.localBanDataManager.setPlayerData(data.playerUuid, data);
                }
            }
        } catch (Exception e) {
            LOGGER.warning("导入本地封禁列表失败: " + e.getMessage());
        }
    }
}
