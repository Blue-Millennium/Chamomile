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
        List<String[]> locallist = new ArrayList<>(List.of());
        List<String[]> reportlist = new ArrayList<>(List.of());
        for (UnionBanData banData : remote) {
            banData.reportTag = true;
            UnionBanData data = dgl.getUnionBanData(banData.playerUuid);
            if (data == null) {
                if (flag || banData.reason.equals("Pardon")) {
                    dgl.setPlayerData(banData.playerUuid, banData);
                    locallist.add(new String[]{banData.playerName, banData.reason});
                }
            } else {
                if (data.time < banData.time) {
                    if (flag || banData.reason.equals("Pardon")) {
                        dgl.setPlayerData(banData.playerUuid, banData);
                        locallist.add(new String[]{banData.playerName, banData.reason});
                    }
                } else if (data.time > banData.time) {
                    if (flag && reportRemoteBanList(data)) {
                        reportlist.add(new String[]{data.playerName, data.reason});
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
                    if (!found || banData.time > time) {
                        if (reportRemoteBanList(banData)) {
                            reportlist.add(new String[]{banData.playerName, banData.reason});
                            banData.reportTag = true;
                        }
                    }
                } catch (Exception e) {
                    LOGGER.warning(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                }
            }
        }
        LocalBanDataProcess();
        String[] localmsg = new String[]{"Local", "的封禁数据已被同步至本地服务器", "的解除封禁数据已被同步至本地服务器"};
        String[] unionmsg = new String[]{"Union", "的封禁数据已被上报至UnionBan服务器", "的解除封禁数据已被上报至UnionBan服务器"};
        MessageSender(locallist, localmsg);
        MessageSender(reportlist, unionmsg);
    }

    public static void MessageSender(List<String[]> namelist, String[] rec) {
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
            BanMessage(rec[0], msg1.append(rec[1]).toString());
            BanMessage(rec[0], msg2.append(rec[2]).toString());
        }
    }
}
