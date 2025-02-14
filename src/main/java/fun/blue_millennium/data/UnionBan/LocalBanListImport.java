package fun.blue_millennium.data.UnionBan;

import fun.blue_millennium.config.Config;
import fun.blue_millennium.data.UnionBan.LocalProcess.UnionBanDataGet;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.profile.PlayerProfile;

import static fun.blue_millennium.Main.LOGGER;
import static fun.blue_millennium.data.UnionBan.OnlineProcess.OnlinePush.reportRemoteBanList;

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
                    data.time = banEntry.getExpiration().getTime();
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
