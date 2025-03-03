package fun.bm.module.impl;

import fun.bm.config.Config;
import fun.bm.data.UnionBan.LocalProcess.UnionBanDataGet;
import fun.bm.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import static fun.bm.data.UnionBan.LocalProcess.LocalBanListImport.importBanList;
import static fun.bm.data.UnionBan.LocalProcess.OnlineDataMerge.mergeAndReportData;

public class UnionBan extends Module {

    public UnionBan() {
        super("UnionBan");
    }

    @Override
    public void onLoad() {
        UnionBanDataGet dg = new UnionBanDataGet();
        dg.load();
        importBanList();
        mergeAndReportData();
    }

    @EventHandler
    public void PlayerJoinProcess(PlayerJoinEvent event) {
        mergeAndReportData();
    }

    public void setModuleName() {
        if (!Config.UnionBanEnabled) {
            this.moduleName = null;
        }
    }
}