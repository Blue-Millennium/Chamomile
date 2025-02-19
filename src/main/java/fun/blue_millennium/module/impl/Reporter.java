package fun.blue_millennium.module.impl;

import fun.blue_millennium.Chamomile;
import fun.blue_millennium.config.Config;
import fun.blue_millennium.module.Module;
import fun.blue_millennium.util.IpinfoUtil;
import fun.blue_millennium.util.map.IpLocationResponse;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import static fun.blue_millennium.Chamomile.LOGGER;

public class Reporter extends Module implements Listener {
    public Group reportGroup = null;

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
                reportGroup = Chamomile.BOT.getGroup(Config.ReportGroup);
            } catch (Exception e) {
                LOGGER.warning("Failed to get report group");
            }

            if (reportGroup == null) {
                LOGGER.warning("Failed to get report group");
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (reportGroup == null) {
            return;
        }

        MessageChainBuilder builder = new MessageChainBuilder();
        String ip = event.getAddress().getHostAddress();
        IpLocationResponse response = IpinfoUtil.getIpinfoCN(ip);

        if (response != null) {
            builder.append(Config.ReportMessage.replace("%NAME%", event.getName()).replace("%IP%", ip).replace("%IPINFO%", response.nation + " " + response.subdivision_1_name + " " + response.subdivision_2_name + " " + response.isp).replace("%RESULT%", event.getLoginResult().toString()));
        } else {
            builder.append(Config.ReportMessage.replace("%NAME%", event.getName()).replace("%IP%", ip).replace("%IPINFO%", "").replace("%RESULT%", event.getLoginResult().toString()));
        }

        reportGroup.sendMessage(builder.build());
    }
}
