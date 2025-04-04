package fun.bm.module.impl.PlayerDataProcess;

import fun.bm.config.Config;
import fun.bm.data.PlayerData.Data;
import fun.bm.data.PlayerData.OldName;
import fun.bm.data.PlayerData.PlayerData;
import fun.bm.module.Module;
import fun.bm.util.MainEnv;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.module.impl.PlayerDataProcess.QQCheck.NullCheck;
import static fun.bm.util.MainEnv.LOGGER;

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
                OldName oldName = new OldName();
                oldName.oldName = data.playerData.playerName;
                oldName.updateTime = System.currentTimeMillis();
                List<OldName> oldNames;
                try {
                    oldNames = data.playerData.oldNames == null ? new ArrayList<>() : data.playerData.oldNames;
                } catch (NullPointerException e) {
                    oldNames = new ArrayList<>();
                }
                oldNames.add(oldName);
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

        MainEnv.dataManager.setPlayerData(event.getUniqueId(), data);
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event == null) {
            return;
        }
        Data data = MainEnv.dataManager.getPlayerData(event.getUniqueId());
        BaseDataProcess(event, data);
    }

    public void setModuleName() {
        if (Config.QQRobotEnabled) {
            this.moduleName = null;
        } else {
            LOGGER.info("QQ机器人未启用，模块" + this.getModuleName() + "将会被加载");
        }
    }
}
