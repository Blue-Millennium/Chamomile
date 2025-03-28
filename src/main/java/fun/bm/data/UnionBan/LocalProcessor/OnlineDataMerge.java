package fun.bm.data.UnionBan.LocalProcessor;

import fun.bm.data.UnionBan.UnionBanData;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.command.main.executor.vanilla.Ban.BanMessage;
import static fun.bm.data.UnionBan.LocalProcessor.BanDataProcess.LocalBanDataProcess;
import static fun.bm.data.UnionBan.OnlineProcessor.OnlineGet.loadRemoteBanList;
import static fun.bm.data.UnionBan.OnlineProcessor.OnlinePush.reportRemoteBanList;

public class OnlineDataMerge {
    public static void mergeAndReportData() {
        UnionBanDataGet dgl = new UnionBanDataGet();
        List<UnionBanData> remote = loadRemoteBanList();
        List<String> namelist1 = new ArrayList<>(List.of());
        List<String> namelist2 = new ArrayList<>(List.of());
        for (UnionBanData banData : remote) {
            UnionBanData data = dgl.getUnionBanData(banData.playerUuid);
            if (data == null) {
                dgl.setPlayerData(banData.playerUuid, banData);
            } else {
                if (data.time < banData.time) {
                    dgl.setPlayerData(banData.playerUuid, banData);
                    namelist1.add(banData.playerName);
                } else if (data.time > banData.time) {
                    if (reportRemoteBanList(banData)) {
                        namelist2.add(banData.playerName);
                    }
                }
            }
        }
        if (!namelist1.isEmpty()) {
            StringBuilder msg = new StringBuilder().append(" ");
            for (String name : namelist2) {
                msg.append(name).append(" ");
            }
            LocalBanDataProcess();
            BanMessage("Local", msg + "的封禁数据已被同步至本地服务器");
        }
        if (!namelist2.isEmpty()) {
            StringBuilder msg = new StringBuilder().append(" ");
            for (String name : namelist2) {
                msg.append(name).append(" ");
            }
            BanMessage("Union", msg + "的封禁数据已被上报至UnionBan服务器");
        }
    }
}
