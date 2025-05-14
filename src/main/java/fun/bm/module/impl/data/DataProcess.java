package fun.bm.module.impl.data;

import fun.bm.config.Config;
import fun.bm.data.manager.data.Data;
import fun.bm.data.manager.data.link.LinkData;
import fun.bm.data.manager.data.link.QQLinkData;
import fun.bm.data.manager.data.link.UseridLinkData;
import fun.bm.data.manager.data.player.OldName;
import fun.bm.data.manager.data.player.PlayerData;
import fun.bm.module.Module;
import fun.bm.util.MainEnv;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.List;

import static fun.bm.module.impl.data.QQCheck.nullCheck;
import static fun.bm.util.MainEnv.LOGGER;

public class DataProcess extends Module {
    public DataProcess() {
        super("DataProcess");
    }

    public static void baseDataProcess(AsyncPlayerPreLoginEvent event, Data data) {
        data = nullCheck(data);
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
                oldNames = data.playerData.oldNames;
                oldNames.add(oldName);
                data.playerData.oldNames = oldNames;
                data.playerData.playerName = event.getName();
            }
        }
        if (!data.linkData.isEmpty()) {
            for (LinkData linkData : data.linkData) {
                if (data.qqChecked && data.useridChecked) break;
                if (linkData instanceof QQLinkData) data.qqChecked = true;
                if (linkData instanceof UseridLinkData) data.useridChecked = true;
            }
        }
        if (data.firstJoin < 0) {
            data.firstJoin = System.currentTimeMillis();
        }
        if (data.firstJoinIp == null) {
            data.firstJoinIp = event.getAddress().getHostAddress();
        }
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
        baseDataProcess(event, data);
    }

    public void setModuleName() {
        if (Config.QQRobotEnabled) {
            this.moduleName = null;
        } else {
            LOGGER.info("QQ机器人未启用，模块" + this.getModuleName() + "将会被加载");
        }
    }
}
