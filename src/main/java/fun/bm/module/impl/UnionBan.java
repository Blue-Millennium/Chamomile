package fun.bm.module.impl;

import fun.bm.config.Config;
import fun.bm.data.manager.unionban.UnionBanData;
import fun.bm.data.manager.unionban.local.UnionBanDataGet;
import fun.bm.module.Module;
import fun.bm.util.MainEnv;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;

import static fun.bm.data.manager.unionban.local.LocalBanListImport.importBanList;
import static fun.bm.data.manager.unionban.local.OnlineDataMerge.mergeAndReportData;

public class UnionBan extends Module {
    public static UnionBanDataGet unionBanDataGet = new UnionBanDataGet();
    public static List<UnionBanData> dataList;
    boolean flag_continue = true;

    public UnionBan() {
        super("UnionBan");
    }

    @Override
    public void onLoad() {
        unionBanDataGet.load();
        importBanList();
        mergeAndReportData(true);
    }

    public void onEnable() {
        if (Config.UnionBanMergePeriod > 0)
            Bukkit.getScheduler().runTaskLater(MainEnv.INSTANCE, this::scheduleTask, Config.UnionBanMergePeriod * 20L);
    }

    public void scheduleTask() {
        mergeAndReportData(true);
        if (flag_continue)
            Bukkit.getScheduler().runTaskLater(MainEnv.INSTANCE, this::scheduleTask, Config.UnionBanMergePeriod * 20L);
    }

    public void onDisable() {
        flag_continue = false;
    }

    @EventHandler
    public void PlayerJoinProcess(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(MainEnv.INSTANCE, () -> mergeAndReportData(true), Config.UnionBanMergePeriod * 20L);
    }

    @EventHandler
    public void preLoginProcess(PlayerLoginEvent event) {
        mergeAndReportData(false);
    }

    public void setModuleName() {
        if (!Config.UnionBanEnabled) {
            this.moduleName = null;
        }
    }
}