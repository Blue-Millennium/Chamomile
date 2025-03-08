package fun.bm.module.impl;

import fun.bm.Chamomile;
import fun.bm.config.Config;
import fun.bm.module.Module;
import fun.bm.util.IpInfoUtil;
import fun.bm.util.map.IpLocationMap;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.Chamomile.LOGGER;

public class Reporter extends Module {
    public static final List<Long> ReportGroups = new ArrayList<>();

    public Reporter() {
        super("Reporter");
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        if (!Config.QQRobotEnabled) {
            try {
                String[] groupIds = Config.ReportGroup.split(";");
                for (String groupId : groupIds) {
                    try {
                        ReportGroups.add(Long.parseLong(groupId.trim()));
                        break;
                    } catch (NumberFormatException e) {
                        LOGGER.warning("[Reporter] Invalid group ID: " + groupId);
                    }
                }
            } catch (Exception e) {
                LOGGER.warning("Failed to get report group");
            }

            if (ReportGroups.isEmpty()) {
                LOGGER.warning("Failed to get report group");
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (ReportGroups.isEmpty()) {
            return;
        }

        MessageChainBuilder builder = new MessageChainBuilder();
        String ip = event.getAddress().getHostAddress();
        IpLocationMap response = IpInfoUtil.getIpinfoCN(ip);

        if (response != null) {
            builder.append(Config.ReportMessage.replace("%NAME%", event.getName()).replace("%IP%", ip).replace("%IPINFO%", response.nation + " " + response.subdivision_1_name + " " + response.subdivision_2_name + " " + response.isp).replace("%RESULT%", event.getLoginResult().toString()));
        } else {
            builder.append(Config.ReportMessage.replace("%NAME%", event.getName()).replace("%IP%", ip).replace("%IPINFO%", "").replace("%RESULT%", event.getLoginResult().toString()));
        }

        for (long groupId : ReportGroups) {
            Group reportGroup = Chamomile.BOT.getGroup(groupId);
            reportGroup.sendMessage(builder.build());
        }
    }

    public void setModuleName() {
        if (!Config.QQRobotEnabled) {
            this.moduleName = null;
        }
    }
}
