package fun.bm.chamomile.function.modules.basedata;

import fun.bm.chamomile.config.modules.Bot.CoreConfig;
import fun.bm.chamomile.data.basedata.BaseData;
import fun.bm.chamomile.data.basedata.link.LinkData;
import fun.bm.chamomile.data.basedata.link.QQLinkData;
import fun.bm.chamomile.data.basedata.link.UseridLinkData;
import fun.bm.chamomile.data.basedata.player.OldName;
import fun.bm.chamomile.data.basedata.player.PlayerData;
import fun.bm.chamomile.function.Function;
import fun.bm.chamomile.util.Environment;
import fun.bm.chamomile.util.TimeUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.chamomile.util.Environment.LOGGER;

public class BaseDataProcessor extends Function {
    public BaseDataProcessor() {
        this("DataProcess");
    }

    public BaseDataProcessor(String moduleName) {
        super(moduleName);
    }

    public static void baseDataProcess(AsyncPlayerPreLoginEvent event, BaseData data) {
        data = Environment.dataManager.baseDataManager.nullCheck(data);
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

        Environment.dataManager.baseDataManager.setPlayerData(event.getUniqueId(), data, true);
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        if (event == null) return;
        BaseData data = Environment.dataManager.baseDataManager.getPlayerData(event.getPlayer().getUniqueId());
        data.lastLogout = TimeUtil.getUnixTimeMs();
        Environment.dataManager.baseDataManager.setPlayerData(event.getPlayer().getUniqueId(), data, true);
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event == null) return;
        BaseData data = Environment.dataManager.baseDataManager.getPlayerData(event.getUniqueId());
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
