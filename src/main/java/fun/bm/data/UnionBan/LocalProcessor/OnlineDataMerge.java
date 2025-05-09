package fun.bm.data.UnionBan.LocalProcessor;

import fun.bm.data.UnionBan.UnionBanData;
import fun.bm.module.impl.UnionBan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fun.bm.command.main.executor.vanilla.Ban.BanMessage;
import static fun.bm.data.UnionBan.LocalProcessor.BanDataProcess.LocalBanDataProcess;
import static fun.bm.data.UnionBan.OnlineProcessor.OnlineGet.loadRemoteBanList;
import static fun.bm.data.UnionBan.OnlineProcessor.OnlinePush.reportRemoteBanList;
import static fun.bm.util.MainEnv.LOGGER;

public class OnlineDataMerge {
    public static void mergeAndReportData(boolean flag) {
        UnionBanDataGet dgl = new UnionBanDataGet();
        List<UnionBanData> remote = loadRemoteBanList();
        List<String> namelist1 = new ArrayList<>(List.of());
        List<String> namelist2 = new ArrayList<>(List.of());
        for (UnionBanData banData : remote) {
            banData.reportTag = true;
            UnionBanData data = dgl.getUnionBanData(banData.playerUuid);
            if (data == null) {
                if (flag || banData.reason.equals("Pardon")) {
                    dgl.setPlayerData(banData.playerUuid, banData);
                    namelist1.add(banData.playerName);
                }
            } else {
                if (data.time < banData.time) {
                    if (flag || banData.reason.equals("Pardon")) {
                        dgl.setPlayerData(banData.playerUuid, banData);
                        namelist1.add(banData.playerName);
                    }
                } else if (data.time > banData.time) {
                    if (flag && reportRemoteBanList(data)) {
                        namelist2.add(data.playerName);
                    }
                }
            }
        }
        if (flag) {
            for (UnionBanData banData : UnionBan.dataList) {
                try {
                    boolean found = false;
                    long time = 0;
                    for (UnionBanData data : remote) {
                        if (data.playerUuid.equals(banData.playerUuid)) {
                            found = true;
                            time = data.time;
                            break;
                        }
                    }
                    if (found && banData.time > time) {
                        banData.reportTag = true;
                        reportRemoteBanList(banData);
                    } else if (!found) {
                        if (reportRemoteBanList(banData)) {
                            namelist2.add(banData.playerName);
                            banData.reportTag = true;
                            reportRemoteBanList(banData);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.warning(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                }
            }
        }
        if (!namelist1.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (String name : namelist1) {
                msg.append(name).append(" ");
            }
            LocalBanDataProcess();
            BanMessage("Local", msg + "的封禁数据已被同步至本地服务器");
        }
        if (!namelist2.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (String name : namelist2) {
                msg.append(name).append(" ");
            }
            BanMessage("Union", msg + "的封禁数据已被上报至UnionBan服务器");
        }
    }
}
