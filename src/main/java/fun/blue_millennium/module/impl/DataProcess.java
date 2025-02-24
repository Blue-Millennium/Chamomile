package fun.blue_millennium.module.impl;

import fun.blue_millennium.Chamomile;
import fun.blue_millennium.data.Data;
import fun.blue_millennium.data.PlayerData;
import fun.blue_millennium.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import static fun.blue_millennium.module.impl.QQCheck.NullCheck;

public class DataProcess extends Module {
    public DataProcess() {
        super("DataProcess");
    }

    public static void BaseDataProcess(AsyncPlayerPreLoginEvent event, Data data) {
        data = NullCheck(data);
        if (data.playerData == null) {
            PlayerData playerData = new PlayerData();
            playerData.playerName = event.getName();
            playerData.playerUuid = event.getUniqueId();
            data.playerData = playerData;
        }
        data.qqChecked = data.qqNumber > 0;
        if (data.firstJoin < 0) {
            data.firstJoin = System.currentTimeMillis();
        }
        if (data.firstJoinIp == null) {
            data.firstJoinIp = event.getAddress().getHostAddress();
        }
        data.qqChecked = data.qqNumber > 0;
        data.useridChecked = data.userid > 0;
        data.lastJoin = System.currentTimeMillis();
        data.lastJoinIp = event.getAddress().getHostAddress();

        Chamomile.INSTANCE.dataManager.setPlayerData(event.getUniqueId(), data);
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event == null) {
            return;
        }
        Data data = Chamomile.INSTANCE.dataManager.getPlayerData(event.getUniqueId());
        BaseDataProcess(event, data);
    }
}
