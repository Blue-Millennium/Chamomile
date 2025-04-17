package fun.bm.module.impl;

import fun.bm.config.Config;
import fun.bm.module.Module;
import fun.bm.util.IpInfoUtil;
import fun.bm.util.MainEnv;
import fun.bm.util.map.IpLocationMap;
import fun.bm.util.map.IpinfoMap;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.util.MainEnv.LOGGER;

public class QQReporter extends Module {
    public static final List<Long> ReportGroups = new ArrayList<>();

    public QQReporter() {
        super("Reporter");
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
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
            IpinfoMap response2 = IpInfoUtil.getIpinfo(ip);
            if (response2 != null) {
                builder.append(Config.ReportMessage.replace("%NAME%", event.getName()).replace("%IP%", ip).replace("%IPINFO%", response2.data.country + " " + response2.data.city + " " + response2.data.region + " " + response2.data.asn).replace("%RESULT%", event.getLoginResult().toString()));
            } else {
                builder.append(Config.ReportMessage.replace("%NAME%", event.getName()).replace("%IP%", ip).replace("%IPINFO%", "").replace("%RESULT%", event.getLoginResult().toString()));
            }
        }

        for (long groupId : ReportGroups) {
            Group reportGroup = MainEnv.BOT.getGroup(groupId);
            reportGroup.sendMessage(builder.build());
        }
    }

    public void setModuleName() {
        if (!Config.QQRobotEnabled || Config.BotModeOfficial) {
            this.moduleName = null;
        }
    }
}
