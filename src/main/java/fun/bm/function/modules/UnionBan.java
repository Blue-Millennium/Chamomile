package fun.bm.function.modules;

import fun.bm.config.modules.UnionBanConfig;
import fun.bm.data.manager.unionban.UnionBanData;
import fun.bm.data.manager.unionban.local.UnionBanDataGet;
import fun.bm.function.Function;
import fun.bm.util.MainEnv;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static fun.bm.data.manager.unionban.local.LocalBanListImport.importBanList;
import static fun.bm.data.manager.unionban.local.OnlineDataMerge.mergeAndReportData;

public class UnionBan extends Function {
    public static UnionBanDataGet unionBanDataGet = new UnionBanDataGet();
    public static List<UnionBanData> dataList;
    private static CompletableFuture<Void> task;
    boolean flag_continue = true;

    public UnionBan() {
        super("UnionBan");
    }

    public void onLoad() {
        CompletableFuture.runAsync(() -> {
            unionBanDataGet.load();
            importBanList();
            mergeAndReportData(true);
        });
    }

    public void onEnable() {
        if (UnionBanConfig.mergePeriod > 0) {
            task.thenAcceptAsync(v -> task = CompletableFuture.runAsync(() -> scheduleTask(true)));
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
        mergeAndReportData(flag);
        if (flag_continue) {
            Bukkit.getScheduler().runTaskLater(MainEnv.INSTANCE, () -> continueTask(false, true), UnionBanConfig.mergePeriod * 20L);
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