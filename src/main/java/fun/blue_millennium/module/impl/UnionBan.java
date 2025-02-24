package fun.blue_millennium.module.impl;

import fun.blue_millennium.config.Config;
import fun.blue_millennium.data.UnionBan.LocalProcess.UnionBanDataGet;
import fun.blue_millennium.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import static fun.blue_millennium.data.UnionBan.LocalProcess.LocalBanListImport.importBanList;
import static fun.blue_millennium.data.UnionBan.LocalProcess.OnlineDataMerge.mergeAndReportData;

public class UnionBan extends Module {

    public UnionBan() {
        super("UnionBan");
    }

    @Override
    public void onLoad() {
        if (Config.UnionBanEnabled) {
            UnionBanDataGet dg = new UnionBanDataGet();
            dg.load();
            importBanList();
            mergeAndReportData();
        }
    }

    @EventHandler
    public void PlayerJoinProcess(PlayerJoinEvent event) {
        if (Config.UnionBanEnabled) {
            mergeAndReportData();
        }
    }
}