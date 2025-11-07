package fun.bm.chamomile.function.modules;

import fun.bm.chamomile.config.modules.Bot.CoreConfig;
import fun.bm.chamomile.config.modules.Bot.ReportConfig;
import fun.bm.chamomile.function.Function;
import fun.bm.chamomile.util.Environment;
import fun.bm.chamomile.util.IpInfoUtil;
import fun.bm.chamomile.util.helper.MainThreadHelper;
import fun.bm.chamomile.util.map.IpLocationMap;
import fun.bm.chamomile.util.map.IpinfoMap;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.chamomile.util.Environment.LOGGER;

public class QQReporter extends Function {
    public static final List<Long> ReportGroups = new ArrayList<>();

    public QQReporter() {
        super("Reporter");
    }

    public void onEnable() {
        try {
            String[] groupIds = ReportConfig.groups.split(";");
            for (String groupId : groupIds) {
                try {
                    String id = groupId.trim();
                    if (id.isEmpty()) {
                        continue;
                    }
                    ReportGroups.add(Long.parseLong(id));
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
            builder.append(ReportConfig.message.replace("%NAME%", event.getName()).replace("%IP%", ip).replace("%IPINFO%", response.nation + " " + response.subdivision_1_name + " " + response.subdivision_2_name + " " + response.isp).replace("%RESULT%", event.getLoginResult().toString()));
        } else {
            IpinfoMap response2 = IpInfoUtil.getIpinfo(ip);
            if (response2 != null) {
                builder.append(ReportConfig.message.replace("%NAME%", event.getName()).replace("%IP%", ip).replace("%IPINFO%", response2.data.country + " " + response2.data.city + " " + response2.data.region + " " + response2.data.asn).replace("%RESULT%", event.getLoginResult().toString()));
            } else {
                builder.append(ReportConfig.message.replace("%NAME%", event.getName()).replace("%IP%", ip).replace("%IPINFO%", "").replace("%RESULT%", event.getLoginResult().toString()));
            }
        }

        if (MainThreadHelper.isBotRunning()) {
            for (long groupId : ReportGroups) {
                Group reportGroup = Environment.BOT.getGroup(groupId);
                reportGroup.sendMessage(builder.build());
            }
        }
    }

    public void setModuleName() {
        if (!CoreConfig.enabled || CoreConfig.official || !ReportConfig.enabled) {
            this.moduleName = null;
        }
    }
}
