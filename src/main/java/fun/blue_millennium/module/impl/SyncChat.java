package fun.blue_millennium.module.impl;

import fun.blue_millennium.Chamomile;
import fun.blue_millennium.config.Config;
import fun.blue_millennium.data.AuthData.DataGet;
import fun.blue_millennium.data.PlayerData;
import fun.blue_millennium.module.Module;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

import static fun.blue_millennium.Chamomile.LOGGER;
import static fun.blue_millennium.message.ImageProcess.sendImageUrl;
import static fun.blue_millennium.util.RconCommandExecute.executeRconCommand;

public class SyncChat extends Module implements Listener {
    public static final List<Long> SyncGroups = new ArrayList<>();

    public SyncChat() {
        super("SyncChat");
    }

    @Override
    public void onEnable() {
        String[] groupIds = Config.SyncChatGroup.split(";");
        for (String groupId : groupIds) {
            try {
                SyncGroups.add(Long.parseLong(groupId.trim()));
            } catch (NumberFormatException e) {
                LOGGER.warning("[SyncChat] Invalid group ID: " + groupId);
            }
        }

        if (!Config.BotModeOfficial) {
            if (SyncGroups.isEmpty()) {
                LOGGER.warning("Failed to get sync group");
                Config.SyncChatEnabled = false;
                Chamomile.INSTANCE.configManager.save();
                return;
            }
        }

        Chamomile.eventChannel.subscribeAlways(GroupMessageEvent.class, event -> {
            for (Long groupId : SyncGroups) {
                Group syncGroup = Chamomile.BOT.getGroup(groupId);
                if (!Config.BotModeOfficial & (!Config.SyncChatEnabled || event.getGroup() != syncGroup)) {
                    return;
                }
            }
            MessageChainBuilder builder = new MessageChainBuilder();
            for (Message message : event.getMessage()) {
                if (message instanceof PlainText || message instanceof At || message instanceof AtAll) {
                    builder.add(message);
                } else if (message instanceof Image image) {
                    builder.add(sendImageUrl(image));
                }
            }

            if (!builder.isEmpty()) {
                if (Config.BotModeOfficial & builder.build().contentToString().replace(" ", "").startsWith(Config.SyncChatStartWord)) {
//                    String avatar = "头像为" + processImageUrl(event.getSender().getAvatarUrl()) + "的QQ用户";
                    String avatar = "QQ用户";
                    String id = GetID(event);
                    String message = Config.SayQQMessage.replace("%NAME%", avatar + id + "发送了以下消息").replace("%MESSAGE%", builder.build().contentToString().replace(Config.SyncChatStartWord, ""));
                    sendMessage(message);
                    event.getGroup().sendMessage("已成功发送消息至服务器，以下为发送至服务器的原始数据：\n" + message);
                } else if (!Config.BotModeOfficial) {
                    sendMessage(Config.SayQQMessage.replace("%NAME%", event.getSenderName()).replace("%MESSAGE%", builder.build().contentToString()));
                }
                if (builder.build().contentToString().replace(" ", "").startsWith(Config.QQCheckStartWord)) {
                    QQCheck.GroupCheck(event, builder);
                }
            }
        });
    }

    public void sendMessage(String message) {
        String command = "tellraw @a \"" + message + "\"";
        executeRconCommand(Config.RconIP, Config.RconPort, Config.RconPassword, command);
    }

    public String GetID(GroupMessageEvent event) {
        DataGet dp = new DataGet();
        List<PlayerData> pd = dp.getPlayerDataByUserID(event.getSender().getId());
        if (pd.isEmpty()) {
            return "(Userid: " + event.getSender().getId() + ")";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("(游戏内ID: ");
            for (PlayerData p : pd) {
                sb.append(p.playerName).append("/");
            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append(")");
            return sb.toString();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Config.SyncChatEnabled & !Config.SyncChatEnabledQ2SOnly & !Config.BotModeOfficial) {
            for (Long groupId : SyncGroups) {
                Group syncGroup = Chamomile.BOT.getGroup(groupId);
                syncGroup.sendMessage(Config.JoinServerMessage.replace("%NAME%", event.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (Config.SyncChatEnabled & !Config.SyncChatEnabledQ2SOnly & !Config.BotModeOfficial) {
            for (Long groupId : SyncGroups) {
                Group syncGroup = Chamomile.BOT.getGroup(groupId);
                syncGroup.sendMessage(Config.LeaveServerMessage.replace("%NAME%", event.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (Config.SyncChatEnabled & !Config.SyncChatEnabledQ2SOnly & !Config.BotModeOfficial) {
            for (Long groupId : SyncGroups) {
                Group syncGroup = Chamomile.BOT.getGroup(groupId);
                syncGroup.sendMessage(Config.SayServerMessage.replace("%NAME%", event.getPlayer().getName()).replace("%MESSAGE%", event.getMessage()));
            }
        }
    }
}
