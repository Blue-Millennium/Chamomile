package fun.blue_millennium.data.UnionBan.LocalProcess;

import fun.blue_millennium.data.UnionBan.UnionBanData;
import org.bukkit.Bukkit;

import java.util.List;

import static fun.blue_millennium.commands.execute.vanilla.Ban.BanMessage;

public class BanDataProcess {
    public static void banDataProcess() {
        UnionBanDataGet dg = new UnionBanDataGet();
        List<UnionBanData> dataList = dg.returnAllData();
        for (UnionBanData data : dataList) {
            if (data.reason.equals("Pardon")) {
                if (Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:pardon " + data.playerUuid)) {
                    String message = "玩家 " + data.playerName + " 已被服务器 " + data.sourceServer + "解除封禁";
                    BanMessage(message);
                }
            } else {
                if (Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:ban " + data.playerUuid + " " + data.reason)) {
                    String message = "玩家 " + data.playerName + " 已被服务器 " + data.sourceServer + "以" + data.reason + "的理由封禁";
                    BanMessage(message);
                }
            }
        }
    }
}
