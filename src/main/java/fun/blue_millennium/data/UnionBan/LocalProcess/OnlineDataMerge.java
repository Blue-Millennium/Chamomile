package fun.blue_millennium.data.UnionBan.LocalProcess;

import fun.blue_millennium.config.Config;
import fun.blue_millennium.data.UnionBan.UnionBanData;
import fun.blue_millennium.message.WebhookForEmail;

import java.util.ArrayList;
import java.util.List;

import static fun.blue_millennium.commands.execute.vanilla.Ban.BanMessage;
import static fun.blue_millennium.data.UnionBan.LocalProcess.BanDataProcess.LocalBanDataProcess;
import static fun.blue_millennium.data.UnionBan.OnlineProcess.OnlineGet.loadRemoteBanList;
import static fun.blue_millennium.data.UnionBan.OnlineProcess.OnlinePush.reportRemoteBanList;

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
            BanMessage(msg + "的封禁数据已被同步至本地服务器");
            WebhookForEmail webhook = new WebhookForEmail();
            webhook.formatAndSendWebhook("本地黑名单: " + msg + "的封禁数据已被同步至本地服务器", msg.toString(), Config.WebHookEmail);
        }
        if (!namelist2.isEmpty()) {
            StringBuilder msg = new StringBuilder().append(" ");
            for (String name : namelist2) {
                msg.append(name).append(" ");
            }
            BanMessage(msg + "的封禁数据已被上报至UnionBan服务器");
            WebhookForEmail webhook = new WebhookForEmail();
            webhook.formatAndSendWebhook("UnionBan: " + msg + "的封禁数据已被上报至UnionBan服务器", msg.toString(), Config.WebHookEmail);
        }
    }
}
