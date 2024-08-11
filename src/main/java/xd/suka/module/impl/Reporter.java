package xd.suka.module.impl;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.AtAll;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jetbrains.annotations.NotNull;
import xd.suka.Main;
import xd.suka.config.Config;
import xd.suka.module.Module;
import xd.suka.util.IpinfoUtil;
import xd.suka.util.TimeUtil;
import xd.suka.util.map.IpinfoMap;

import static xd.suka.Main.LOGGER;

public class Reporter extends Module implements Listener {
    private Group reportGroup = null;

    public Reporter() {
        super("Reporter");
    }

    @Override
    public void onLoad() {
        Command command = new Command("bpReport") {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if (args.length == 0) {
                    sender.sendMessage("§c/bpReport <玩家名> <原因>");
                } else {
                    if (reportGroup != null) {
                        MessageChainBuilder builder = new MessageChainBuilder();
                        String number = String.valueOf(System.currentTimeMillis());
                        if (reportGroup.getBotPermission().getLevel() > 0) {
                            builder.append(AtAll.INSTANCE).append(" ");
                        }
                        builder.append(TimeUtil.getNowTime()).append('\n').append("玩家 ").append(args[0]).append(" 被 ").append(sender.getName()).append(" 报告，原因：").append(args.length > 1 ? args[1] : "无").append('\n').append("编号: ").append(number);
                        reportGroup.sendMessage(builder.build());
                        sender.sendMessage("§a已发送报告  编号: " + number);
                    } else {
                        sender.sendMessage("§c内部错误");
                    }
                }
                return true;
            }
        };

        Bukkit.getCommandMap().register("r", "bp", command);
        Bukkit.getCommandMap().register("report", "bp", command);
    }

    @Override
    public void onEnable() {
        reportGroup = Main.INSTANCE.BOT.getGroup(Config.reportGroup);

        if (reportGroup == null) {
            LOGGER.error("Failed to get report group");
            return;
        }
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (reportGroup == null) {
            return;
        }

        MessageChainBuilder builder = new MessageChainBuilder();
        IpinfoMap ipinfoMap = IpinfoUtil.getIpinfo(event.getAddress().getHostAddress());

        builder.append(event.getName()).append(" was logging in").append('\n').append("IP: ").append(event.getAddress().getHostAddress()).append(" ");
        if (ipinfoMap != null && ipinfoMap.error.isEmpty()) {
            builder.append(ipinfoMap.data.region).append(" ").append(ipinfoMap.data.city).append(" ").append("Hosting: ").append(String.valueOf(ipinfoMap.data.privacy.hosting));
        }
        builder.append('\n').append("LoginResult: ").append(event.getLoginResult().toString());

        reportGroup.sendMessage(builder.build());
    }
}
