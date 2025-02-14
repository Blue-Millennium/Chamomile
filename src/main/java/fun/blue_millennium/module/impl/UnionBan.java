package fun.blue_millennium.module.impl;

import fun.blue_millennium.config.Config;
import fun.blue_millennium.data.UnionBan.LocalProcess.UnionBanDataGet;
import fun.blue_millennium.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static fun.blue_millennium.data.UnionBan.LocalProcess.OnlineDataMerge.mergeData;

public class UnionBan extends Module implements Listener {

    public UnionBan() {
        super("UnionBan");
    }

    @Override
    public void onLoad() {
        if (Config.UnionBanEnabled) {
            UnionBanDataGet dg = new UnionBanDataGet();
            dg.load();
            mergeData();
        }
    }

    @EventHandler
    public void PlayerJoinProcess(PlayerJoinEvent event) {
        if (Config.UnionBanEnabled) {
            mergeData();
        }
    }
}