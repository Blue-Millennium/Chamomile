package fun.bm.data.DataManager.UnionBan.Local;

import fun.bm.config.Config;
import fun.bm.data.DataManager.UnionBan.UnionBanData;
import fun.bm.module.impl.UnionBan;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.profile.PlayerProfile;

import java.util.Date;

import static fun.bm.data.DataManager.UnionBan.Online.OnlinePush.reportRemoteBanList;
import static fun.bm.util.MainEnv.LOGGER;

public class LocalBanListImport {
    public static void importBanList() {
        try {
            ProfileBanList banlist = Bukkit.getBanList(BanList.Type.PROFILE);
            for (BanEntry<PlayerProfile> banEntry : banlist.getEntries()) {
                UnionBanData data = UnionBan.unionBanDataGet.getUnionBanData(banEntry.getBanTarget().getUniqueId());
                if (data == null) {
                    data = new UnionBanData();
                    data.playerName = banEntry.getBanTarget().getName();
                    data.playerUuid = banEntry.getBanTarget().getUniqueId();
                    Date expiration = banEntry.getExpiration();
                    data.time = expiration != null ? expiration.getTime() : System.currentTimeMillis();
                    data.reason = banEntry.getReason();
                    data.sourceServer = Config.ServerName;
                    data.localTag = true;
                    if (reportRemoteBanList(data)) {
                        data.reportTag = true;
                    }
                    UnionBan.unionBanDataGet.setPlayerData(data.playerUuid, data);
                }
            }
        } catch (Exception e) {
            LOGGER.warning("导入本地封禁列表失败: " + e.getMessage());
        }
    }
}
