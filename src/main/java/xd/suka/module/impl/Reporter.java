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
        builder.append(event.getName()).append(" was logging in").append('\n').append("IP: ").append(ip);
        IpLocationResponse response = IpinfoUtil.getIpinfoCN(ip);

        if (response != null) {
            builder.append(" ").append(response.nation).append(" ").append(response.subdivision_1_name).append(" ").append(response.subdivision_2_name).append(" ").append(response.isp);
        }
        builder.append('\n').append("LoginResult: ").append(event.getLoginResult().toString());

        reportGroup.sendMessage(builder.build());
    }
}
