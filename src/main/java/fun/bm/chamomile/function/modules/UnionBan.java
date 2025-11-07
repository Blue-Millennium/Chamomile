package fun.bm.chamomile.function.modules;

import fun.bm.chamomile.config.modules.UnionBanConfig;
import fun.bm.chamomile.data.manager.unionban.CrossRegionDataManager;
import fun.bm.chamomile.data.manager.unionban.LocalDataManager;
import fun.bm.chamomile.data.manager.unionban.OnlineDataManager;
import fun.bm.chamomile.data.manager.unionban.UnionBanData;
import fun.bm.chamomile.function.Function;
import fun.bm.chamomile.util.Environment;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UnionBan extends Function {
    public static LocalDataManager localBanDataManager = new LocalDataManager();
    public static OnlineDataManager onlineBanDataManager = new OnlineDataManager();
    public static CrossRegionDataManager crossRegionBanDataManager = new CrossRegionDataManager();
    public static List<UnionBanData> dataList;
    private static CompletableFuture<Void> task;
    boolean flag_continue = true;

    public UnionBan() {
        super("UnionBan");
    }

    public void onLoad() {
        CompletableFuture.runAsync(() -> {
            localBanDataManager.load();
            crossRegionBanDataManager.mergeAndReportData(true);
        });
    }

    public void onEnable() {
        if (UnionBanConfig.mergePeriod > 0) {
            task = CompletableFuture.runAsync(() -> scheduleTask(true));
        }
    }

    public void continueTask(boolean flag1, boolean flag2) {
        if (UnionBanConfig.mergePeriod > 0) {
            if (task != null) {
                if (task.isDone()) {
                    task = CompletableFuture.runAsync(() -> scheduleTask(flag2));
                } else if (flag1) {
                    task.thenAccept(v -> task = CompletableFuture.runAsync(() -> scheduleTask(flag2)));
                }
            } else {
                task = CompletableFuture.runAsync(() -> scheduleTask(flag2));
            }
        }
    }

    public void scheduleTask(boolean flag) {
        crossRegionBanDataManager.mergeAndReportData(flag);
        if (flag_continue) {
            Bukkit.getScheduler().runTaskLater(Environment.INSTANCE, () -> continueTask(false, true), UnionBanConfig.mergePeriod * 20L);
        }
    }

    public void onDisable() {
        flag_continue = false;
    }

    @EventHandler
    public void PlayerJoinProcess(PlayerJoinEvent event) {
        continueTask(true, true);
    }

    @EventHandler
    public void preLoginProcess(PlayerLoginEvent event) {
        continueTask(false, false);
    }

    public void setModuleName() {
        if (!UnionBanConfig.enabled) {
            this.moduleName = null;
        }
    }
}