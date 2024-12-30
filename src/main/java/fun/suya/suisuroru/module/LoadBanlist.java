package fun.suya.suisuroru.module;

import fun.suya.suisuroru.config.Config;
import fun.suya.suisuroru.data.UnionBan.LocalCache;
import fun.suya.suisuroru.data.UnionBan.UnionBanMain;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static fun.suya.suisuroru.commands.execute.vanilla.Ban.BanMessage;

/**
 * @author Suisuroru
 * Date: 2024/10/28 23:08
 * function: Load banlist when player join
 */
public class LoadBanlist implements Listener {

    @EventHandler
    public void PlayerJoinMessage(PlayerJoinEvent event) {
        if (Config.UnionBanEnabled) {
            try {
                List<UnionBanMain.BanPair<UUID, String, Date, String>> Cache = LocalCache.read();

                if (!Cache.isEmpty()) {
                    for (UnionBanMain.BanPair<UUID, String, Date, String> data : Cache) {

                        if ("Pardon".equals(data.getSourceServer())) {
                            boolean result = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:pardon " + data.getUUID());
                            if (result) {
                                String message;
                                try {
                                    message = "玩家 " + Bukkit.getPlayer(data.getUUID()).getName() + " 已被 " + data.getSourceServer() + "解除封禁";
                                } catch (Exception e) {
                                    message = "玩家 [UUID]" + data.getUUID() + " 已被 " + data.getSourceServer() + "解除封禁";
                                }
                                BanMessage(message);
                            }
                        } else if ("Ban".equals(data.getSourceServer())) {
                            data.changeSource(Config.servername);
                            boolean result = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:ban " + data.getUUID() + " " + data.getReason() + "-Banned by " + data.getSourceServer());
                            if (result) {
                                String message;
                                try {
                                    message = "玩家 " + Bukkit.getPlayer(data.getUUID()).getName() + " 已被 " + data.getSourceServer() + " 以[ " + data.getReason() + " ]的理由封禁";
                                } catch (Exception e1) {
                                    message = "玩家 [UUID]" + data.getUUID() + " 已被 " + data.getSourceServer() + " 以[ " + data.getReason() + " ]的理由封禁";
                                }
                                BanMessage(message);
                            }
                        }
                        UnionBanMain.BanPair<UUID, String, Date, String> CachePair = new UnionBanMain.BanPair<>(data.getUUID(), data.getReason(), data.getTime(), data.getSourceServer());
                        UnionBanMain.reportBanData(CachePair);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}