package fun.bm.data.UnionBan.LocalProcess;

import fun.bm.data.UnionBan.UnionBanData;
import org.bukkit.Bukkit;

import java.util.UUID;

import static fun.bm.data.UnionBan.LocalProcess.OnlineDataMerge.mergeAndReportData;


public class ReportedDataProcess {
    public static void reportBanData(String name, UUID uuid, Long time, String reason, String sourceServer) {
        Bukkit.broadcastMessage("正在尝试与UnionBan服务器合并封禁数据");
        UnionBanData data = new UnionBanData();
        data.playerName = name;
        data.playerUuid = uuid;
        data.time = time;
        data.reason = reason;
        data.sourceServer = sourceServer;
        data.reportTag = false;
        data.localTag = true;
        UnionBanDataGet dg = new UnionBanDataGet();
        dg.setPlayerData(uuid, data);
        mergeAndReportData();
    }
}
