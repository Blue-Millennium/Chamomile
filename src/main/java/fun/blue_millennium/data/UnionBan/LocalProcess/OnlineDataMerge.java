package fun.blue_millennium.data.UnionBan.LocalProcess;

import fun.blue_millennium.data.UnionBan.OnlineProcess.OnlinePush;
import fun.blue_millennium.data.UnionBan.UnionBanData;

import java.util.List;

import static fun.blue_millennium.data.UnionBan.OnlineProcess.OnlineGet.loadRemoteBanList;

public class OnlineDataMerge {
    public static void mergeData() {
        UnionBanDataGet dgl = new UnionBanDataGet();
        List<UnionBanData> remote = loadRemoteBanList();
        for (UnionBanData banData : remote) {
            UnionBanData data = dgl.getUnionBanData(banData.playerUuid);
            if (data == null) {
                dgl.setPlayerData(banData.playerUuid, banData);
            } else {
                if (data.time < banData.time) {
                    dgl.setPlayerData(banData.playerUuid, banData);
                } else if (data.time > banData.time) {
                    OnlinePush.reportRemoteBanList(banData);
                }
            }
        }
    }
}
