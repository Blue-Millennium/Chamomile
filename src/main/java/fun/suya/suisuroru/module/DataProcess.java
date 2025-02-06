package fun.suya.suisuroru.module;

import fun.xd.suka.Main;
import fun.xd.suka.data.Data;
import fun.xd.suka.data.PlayerData;
import fun.xd.suka.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import static fun.xd.suka.module.impl.QQCheck.NullCheck;

public class DataProcess extends Module implements Listener {
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
        if (data.qqNumber > 0) {
            data.qqChecked = true;
        }
        data.lastJoin = System.currentTimeMillis();
        data.lastJoinIp = event.getAddress().getHostAddress();

        Main.INSTANCE.dataManager.setPlayerData(event.getUniqueId(), data);
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event == null) {
            return;
        }
        Data data = Main.INSTANCE.dataManager.getPlayerData(event.getUniqueId());
        BaseDataProcess(event, data);
    }
}
