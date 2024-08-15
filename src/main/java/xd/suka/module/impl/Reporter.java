package xd.suka.module.impl;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import xd.suka.Main;
import xd.suka.config.Config;
import xd.suka.module.Module;
import xd.suka.util.IpinfoUtil;
import xd.suka.util.map.IpLocationResponse;

import static xd.suka.Main.LOGGER;

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
        reportGroup = Main.INSTANCE.BOT.getGroup(Config.reportGroup);

        if (reportGroup == null) {
            LOGGER.warning("Failed to get report group");
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
            builder.append(Config.reportMessage.replace("%NAME%", event.getName()).replace("%IP%", ip).replace("%IPINFO%", response.nation + " " + response.subdivision_1_name + " " + response.subdivision_2_name + " " + response.isp).replace("%RESULT%", event.getLoginResult().toString()));
        } else {
            builder.append(Config.reportMessage.replace("%NAME%", event.getName()).replace("%IP%", ip).replace("%IPINFO%", "").replace("%RESULT%", event.getLoginResult().toString()));
        }

        reportGroup.sendMessage(builder.build());
    }
}
