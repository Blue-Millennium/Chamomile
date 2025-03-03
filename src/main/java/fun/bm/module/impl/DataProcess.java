package fun.bm.module.impl;

import fun.bm.Chamomile;
import fun.bm.config.Config;
import fun.bm.data.Data;
import fun.bm.data.OldUsedName;
import fun.bm.data.PlayerData;
import fun.bm.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.module.impl.QQCheck.NullCheck;

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
        } else {
            if (!data.playerData.playerName.equals(event.getName())) {
                OldUsedName oldUsedName = new OldUsedName();
                oldUsedName.oldName = data.playerData.playerName;
                oldUsedName.updateTime = System.currentTimeMillis();
                List<OldUsedName> oldNames;
                try {
                    oldNames = data.playerData.oldNames == null ? new ArrayList<>() : data.playerData.oldNames;
                } catch (NullPointerException e) {
                    oldNames = new ArrayList<>();
                }
                oldNames.add(oldUsedName);
                data.playerData.oldNames = oldNames;
                data.playerData.playerName = event.getName();
            }
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

    public void setModuleName() {
        if (Config.QQRobotEnabled) {
            this.moduleName = null;
        }
    }
}
