package fun.bm.chamomile.data.unionban;

import fun.bm.chamomile.function.modules.UnionBan;
import fun.bm.chamomile.util.Environment;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static fun.bm.chamomile.command.modules.vanilla.Ban.BanMessage;
import static fun.bm.chamomile.util.Environment.LOGGER;

public class CrossRegionDataManager {
    public void mergeAndReportData(boolean flag) {
        List<UnionBanData> remote = Environment.dataManager.unionBanDataManager.onlineBanDataManager.loadRemoteBanList();
        List<String[]> locallist = new ArrayList<>();
        List<String[]> reportlist = new ArrayList<>();
        for (UnionBanData banData : remote) {
            UnionBanData data = Environment.dataManager.unionBanDataManager.localBanDataManager.getUnionBanData(banData.playerUuid);
            if (data == null) {
                if (flag || banData.reason.equals("Pardon")) {
                    Environment.dataManager.unionBanDataManager.localBanDataManager.setPlayerData(banData.playerUuid, banData);
                    locallist.add(new String[]{banData.playerName, banData.reason});
                }
            } else {
                if (data.time < banData.time && (flag || banData.reason.equals("Pardon"))) {
                    Environment.dataManager.unionBanDataManager.localBanDataManager.setPlayerData(banData.playerUuid, banData);
                    locallist.add(new String[]{banData.playerName, banData.reason});
                } else if (data.time > banData.time && flag && Environment.dataManager.unionBanDataManager.onlineBanDataManager.reportRemoteBanList(data)) {
                    reportlist.add(new String[]{data.playerName, data.reason});
                    banData.reportTag = true;
                    Environment.dataManager.unionBanDataManager.localBanDataManager.setPlayerData(data.playerUuid, data);
                }
            }
        }
        if (flag) {
            remote = Environment.dataManager.unionBanDataManager.onlineBanDataManager.loadRemoteBanList();
            for (UnionBanData banData : UnionBan.dataList) {
                try {
                    boolean found = false;
                    for (UnionBanData data : remote) {
                        if (data.playerUuid.equals(banData.playerUuid)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found && Environment.dataManager.unionBanDataManager.onlineBanDataManager.reportRemoteBanList(banData)) {
                        reportlist.add(new String[]{banData.playerName, banData.reason});
                        banData.reportTag = true;
                        Environment.dataManager.unionBanDataManager.localBanDataManager.setPlayerData(banData.playerUuid, banData);
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

    public void messageSender(List<String[]> namelist, String[] rec) {
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

    public void reportBanMessage(StringBuilder msg, String rec1, String rec2) {
        if (!msg.isEmpty()) BanMessage(rec1, msg.append(rec2).toString());
    }

    public void localBanDataProcess() {
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
                Environment.dataManager.unionBanDataManager.localBanDataManager.setPlayerData(data.playerUuid, data);
                BanMessage("Local", message);
            }
        }
    }

    public void reportBanData(String name, UUID uuid, long time, String reason, String sourceServer) {
        Bukkit.broadcastMessage("正在尝试与UnionBan服务器合并封禁与解封数据");
        UnionBanData data = new UnionBanData();
        data.playerName = name;
        data.playerUuid = uuid;
        data.time = time;
        data.reason = reason;
        data.sourceServer = sourceServer;
        data.reportTag = false;
        data.localTag = true;
        Environment.dataManager.unionBanDataManager.localBanDataManager.setPlayerData(uuid, data);
        mergeAndReportData(true);
    }
}
