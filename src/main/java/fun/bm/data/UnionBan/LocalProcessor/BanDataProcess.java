package fun.bm.data.UnionBan.LocalProcessor;

import fun.bm.data.UnionBan.UnionBanData;
import fun.bm.module.impl.UnionBan;
import org.bukkit.Bukkit;

import static fun.bm.command.main.executor.vanilla.Ban.BanMessage;

public class BanDataProcess {
    public static void LocalBanDataProcess() {
        UnionBanDataGet dg = new UnionBanDataGet();
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
                dg.setPlayerData(data.playerUuid, data);
                BanMessage("Local", message);
            }
        }
    }
}
