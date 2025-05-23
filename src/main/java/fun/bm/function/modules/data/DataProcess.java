package fun.bm.function.modules.data;

import fun.bm.config.modules.Bot.CoreConfig;
import fun.bm.data.manager.data.Data;
import fun.bm.data.manager.data.link.LinkData;
import fun.bm.data.manager.data.link.QQLinkData;
import fun.bm.data.manager.data.link.UseridLinkData;
import fun.bm.data.manager.data.player.OldName;
import fun.bm.data.manager.data.player.PlayerData;
import fun.bm.function.Function;
import fun.bm.util.MainEnv;
import fun.bm.util.TimeUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.util.MainEnv.LOGGER;

public class DataProcess extends Function {
    public DataProcess() {
        super("DataProcess");
    }

    public static void baseDataProcess(AsyncPlayerPreLoginEvent event, Data data) {
        data = MainEnv.dataManager.nullCheck(data);
        if (data.playerData == null) {
            PlayerData playerData = new PlayerData();
            playerData.playerName = event.getName();
            playerData.playerUuid = event.getUniqueId();
            data.playerData = playerData;
        } else {
            if (!data.playerData.playerName.equals(event.getName())) {
                OldName oldName = new OldName(data.playerData.playerName, TimeUtil.getUnixTimeMs());
                List<OldName> oldNames;
                oldNames = data.playerData.oldNames == null ? new ArrayList<>() : data.playerData.oldNames;
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
            data.firstJoin = TimeUtil.getUnixTimeMs();
        }
        if (data.firstJoinIp == null) {
            data.firstJoinIp = event.getAddress().getHostAddress();
        }
        data.lastJoin = TimeUtil.getUnixTimeMs();
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
        if (CoreConfig.enabled) {
            this.moduleName = null;
        } else {
            LOGGER.info("QQ机器人未启用，模块" + this.getModuleName() + "将会被加载");
        }
    }
}
