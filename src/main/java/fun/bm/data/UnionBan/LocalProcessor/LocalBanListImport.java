package fun.bm.data.UnionBan.LocalProcessor;

import fun.bm.config.Config;
import fun.bm.data.UnionBan.UnionBanData;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.profile.PlayerProfile;

import java.util.Date;

import static fun.bm.data.UnionBan.OnlineProcessor.OnlinePush.reportRemoteBanList;
import static fun.bm.util.MainEnv.LOGGER;

public class LocalBanListImport {
    public static void importBanList() {
        try {
            ProfileBanList banlist = Bukkit.getBanList(BanList.Type.PROFILE);
            UnionBanDataGet dg = new UnionBanDataGet();
            for (BanEntry<PlayerProfile> banEntry : banlist.getEntries()) {
                UnionBanData data = dg.getUnionBanData(banEntry.getBanTarget().getUniqueId());
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
                    dg.setPlayerData(data.playerUuid, data);
                }
            }
        } catch (Exception e) {
            LOGGER.warning("导入本地封禁列表失败: " + e.getMessage());
        }
    }
}
