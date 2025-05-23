package fun.bm.data.manager.unionban.local;

import fun.bm.data.manager.unionban.UnionBanData;
import fun.bm.function.modules.UnionBan;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static fun.bm.command.modules.executor.vanilla.Ban.BanMessage;
import static fun.bm.data.manager.unionban.online.OnlineGet.loadRemoteBanList;
import static fun.bm.data.manager.unionban.online.OnlinePush.reportRemoteBanList;
import static fun.bm.util.MainEnv.LOGGER;

public class OnlineDataMerge {
    public static void mergeAndReportData(boolean flag) {
        List<UnionBanData> remote = loadRemoteBanList();
        List<String[]> locallist = new ArrayList<>();
        List<String[]> reportlist = new ArrayList<>();
        for (UnionBanData banData : remote) {
            UnionBanData data = UnionBan.unionBanDataGet.getUnionBanData(banData.playerUuid);
            if (data == null) {
                if (flag || banData.reason.equals("Pardon")) {
                    UnionBan.unionBanDataGet.setPlayerData(banData.playerUuid, banData);
                    locallist.add(new String[]{banData.playerName, banData.reason});
                }
            } else {
                if (data.time < banData.time && (flag || banData.reason.equals("Pardon"))) {
                    UnionBan.unionBanDataGet.setPlayerData(banData.playerUuid, banData);
                    locallist.add(new String[]{banData.playerName, banData.reason});
                } else if (data.time > banData.time && flag && reportRemoteBanList(data)) {
                    reportlist.add(new String[]{data.playerName, data.reason});
                    banData.reportTag = true;
                    UnionBan.unionBanDataGet.setPlayerData(data.playerUuid, data);
                }
            }
        }
        if (flag) {
            remote = loadRemoteBanList();
            for (UnionBanData banData : UnionBan.dataList) {
                try {
                    boolean found = false;
                    for (UnionBanData data : remote) {
                        if (data.playerUuid.equals(banData.playerUuid)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found && reportRemoteBanList(banData)) {
                        reportlist.add(new String[]{banData.playerName, banData.reason});
                        banData.reportTag = true;
                        UnionBan.unionBanDataGet.setPlayerData(banData.playerUuid, banData);
                    }
                } catch (Exception e) {
                    LOGGER.warning(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                }
            }
        }
        localBanDataProcess();
        String[] localmsg = new String[]{"Local", "的封禁数据已被同步至本地服务器", "的解除封禁数据已被同步至本地服务器"};
        String[] unionmsg = new String[]{"Union", "的封禁数据已被上报至UnionBan服务器", "的解除封禁数据已被上报至UnionBan服务器"};
        messageSender(locallist, localmsg);
        messageSender(reportlist, unionmsg);
    }

    public static void messageSender(List<String[]> namelist, String[] rec) {
        if (!namelist.isEmpty()) {
            StringBuilder msg1 = new StringBuilder();
            StringBuilder msg2 = new StringBuilder();
            for (String[] name : namelist) {
                if (name[1].equals("Pardon")) {
                    msg2.append(name[0]).append(" ");
                } else {
                    msg1.append(name[0]).append(" ");
                }
            }
            reportBanMessage(msg1, rec[0], rec[1]);
            reportBanMessage(msg2, rec[0], rec[2]);
        }
    }

    public static void reportBanMessage(StringBuilder msg, String rec1, String rec2) {
        if (!msg.isEmpty()) BanMessage(rec1, msg.append(rec2).toString());
    }

    public static void localBanDataProcess() {
        for (UnionBanData data : UnionBan.dataList) {
            if (!data.localTag) {
                String message = "";
                if (data.reason.equals("Pardon")) {
                    if (Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:pardon " + data.playerName)) {
                        message = "玩家 " + data.playerName + " 已被服务器 " + data.sourceServer + "解除封禁";
                    }
                } else {
                    if (Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:ban " + data.playerName + " " + data.reason)) {
                        message = "玩家 " + data.playerName + " 已被服务器 " + data.sourceServer + "以" + data.reason + "的理由封禁";
                    }
                }
                data.localTag = true;
                UnionBan.unionBanDataGet.setPlayerData(data.playerUuid, data);
                BanMessage("Local", message);
            }
        }
    }

    public static void reportBanData(String name, UUID uuid, long time, String reason, String sourceServer) {
        Bukkit.broadcastMessage("正在尝试与UnionBan服务器合并封禁与解封数据");
        UnionBanData data = new UnionBanData();
        data.playerName = name;
        data.playerUuid = uuid;
        data.time = time;
        data.reason = reason;
        data.sourceServer = sourceServer;
        data.reportTag = false;
        data.localTag = true;
        UnionBan.unionBanDataGet.setPlayerData(uuid, data);
        mergeAndReportData(true);
    }
}
