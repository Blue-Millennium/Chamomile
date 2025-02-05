package fun.xd.suka.module.impl;

import fun.xd.suka.Main;
import fun.xd.suka.data.Data;
import fun.xd.suka.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class DataProcess extends Module implements Listener {
    public DataProcess() {
        super("DataProcess");
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event == null) {
            return;
        }
        Data data = Main.INSTANCE.dataManager.getPlayerData(event.getUniqueId());
        BaseDataProcess(event, data);
    }

    static void BaseDataProcess(AsyncPlayerPreLoginEvent event, Data data) {
        if (data.firstJoin < 0) {
            data.firstJoin = System.currentTimeMillis();
        }
        if (data.firstJoinIp == null) {
            data.firstJoinIp = event.getAddress().getHostAddress();
        }

        data.lastJoin = System.currentTimeMillis();
        data.lastJoinIp = event.getAddress().getHostAddress();

        Main.INSTANCE.dataManager.setPlayerData(event.getUniqueId(), data);
    }
}
