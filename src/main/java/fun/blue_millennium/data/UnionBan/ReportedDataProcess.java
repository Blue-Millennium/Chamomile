package fun.blue_millennium.data.UnionBan;

import java.util.UUID;


public class ReportedDataProcess {
    public static void reportBanData(String name, UUID uuid, Long time, String reason, String sourceServer) {
        UnionBanData data = new UnionBanData();
        data.playerName = name;
        data.playerUuid = uuid;
        data.time = time;
        data.reason = reason;
        data.sourceServer = sourceServer;
        UnionBanDataGet dg = new UnionBanDataGet();
        dg.setPlayerData(uuid, data);
    }
}
