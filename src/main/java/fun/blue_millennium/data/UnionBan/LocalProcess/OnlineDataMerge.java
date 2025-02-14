package fun.blue_millennium.data.UnionBan.LocalProcess;

import fun.blue_millennium.data.UnionBan.OnlineProcess.OnlinePush;
import fun.blue_millennium.data.UnionBan.UnionBanData;

import java.util.ArrayList;
import java.util.List;

import static fun.blue_millennium.commands.execute.vanilla.Ban.BanMessage;
import static fun.blue_millennium.data.UnionBan.OnlineProcess.OnlineGet.loadRemoteBanList;

public class OnlineDataMerge {
    public static void mergeData() {
        UnionBanDataGet dgl = new UnionBanDataGet();
        List<UnionBanData> remote = loadRemoteBanList();
        List<String> namelist = new ArrayList<>(List.of());
        for (UnionBanData banData : remote) {
            UnionBanData data = dgl.getUnionBanData(banData.playerUuid);
            if (data == null) {
                dgl.setPlayerData(banData.playerUuid, banData);
            } else {
                if (data.time < banData.time) {
                    dgl.setPlayerData(banData.playerUuid, banData);
                } else if (data.time > banData.time) {
                    if (OnlinePush.reportRemoteBanList(banData)) {
                        namelist.add(banData.playerName);
                    }
                }
            }
        }
        if (!namelist.isEmpty()) {
            StringBuilder msg = new StringBuilder().append(" ");
            for (String name : namelist) {
                msg.append(name).append(" ");
            }
            BanMessage(msg + "的封禁数据已被上报至UnionBan服务器");
        }
    }
}
