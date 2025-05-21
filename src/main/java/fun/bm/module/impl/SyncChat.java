package fun.bm.module.impl;

import fun.bm.config.old.Config;
import fun.bm.data.manager.data.Data;
import fun.bm.data.manager.data.link.UseridLinkData;
import fun.bm.module.Module;
import fun.bm.module.impl.data.QQCheck;
import fun.bm.util.MainEnv;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.command.main.executor.extra.sub.data.Query.dataGet;
import static fun.bm.data.processor.report.ImageProcessor.processImageUrl;
import static fun.bm.data.processor.report.ImageProcessor.sendImageUrl;
import static fun.bm.util.MainEnv.LOGGER;
import static fun.bm.util.helper.RconHelper.executeRconCommand;

public class SyncChat extends Module {
    public static final List<Long> SyncGroups = new ArrayList<>();

    public SyncChat() {
        super("SyncChat");
    }

    public void onEnable() {
        String[] groupIds = Config.SyncChatGroup.split(";");
        for (String groupId : groupIds) {
            try {
                String id = groupId.trim();
                if (id.isEmpty()) {
                    continue;
                }
                SyncGroups.add(Long.parseLong(id));
            } catch (NumberFormatException e) {
                LOGGER.warning("[SyncChat] Invalid group ID: " + groupId);
            }
        }

        if (!Config.BotModeOfficial) {
            if (SyncGroups.isEmpty()) {
                LOGGER.warning("Failed to get sync group");
                Config.SyncChatEnabled = false;
                MainEnv.configManager.save();
                return;
            }
        }

        MainEnv.eventChannel.subscribeAlways(GroupMessageEvent.class, event -> {
            for (long groupId : SyncGroups) {
                Group syncGroup = MainEnv.BOT.getGroup(groupId);
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
                if (Config.BotModeOfficial & builder.build().contentToString().replace(" ", "").startsWith(Config.SyncChatStartWord.replace(" ", ""))) {
                    while (builder.build().contentToString().startsWith(" ")) builder.remove(0);
                    String avatar = "QQ用户" + processImageUrl(event.getSender().getAvatarUrl());
                    String id = getID(event);
                    String message = Config.SayQQMessage.replace("%NAME%", avatar + id + "发送了以下消息").replace("%MESSAGE%", builder.build().contentToString().replace(Config.SyncChatStartWord, ""));
                    sendMessage(message);
                    event.getGroup().sendMessage("已成功发送消息至服务器，以下为发送至服务器的原始数据：\n" + message.replaceAll("\\[\\[CICode,url=[^\\]]*\\]\\]", "[图片]"));
                } else if (!Config.BotModeOfficial) {
                    sendMessage(Config.SayQQMessage.replace("%NAME%", event.getSenderName()).replace("%MESSAGE%", builder.build().contentToString()));
                }
                if (builder.build().contentToString().replace(" ", "").startsWith(Config.QQCheckStartWord.replace(" ", ""))) {
                    QQCheck.groupCheck(event, builder);
                }
            }
        });
    }

    public void sendMessage(String message) {
        String command = "tellraw @a \"" + message + "\"";
        executeRconCommand(Config.RconIP, Config.RconPort, Config.RconPassword, command);
    }

    public String getID(GroupMessageEvent event) {
        List<String> pd = new ArrayList<>();
        List<Data> dataList = dataGet.getPlayersByUserID(event.getGroup().getId());
        if (!dataList.isEmpty()) {
            for (Data data : dataList) {
                if (!data.linkData.isEmpty()
                        && data.linkData.stream()
                        .anyMatch(linkData -> (Config.BotModeOfficial && linkData instanceof UseridLinkData
                                && ((UseridLinkData) linkData).userid == event.getSender().getId())))
                    pd.add(data.playerData.playerName);
            }
        }
        if (pd.isEmpty()) {
            return "(Userid: " + event.getSender().getId() + ")";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("(游戏内ID: ");
            for (String p : pd) {
                sb.append(p).append("/");
            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append(", Userid: ").append(event.getSender().getId()).append(")");
            return sb.toString();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (needSync()) {
            for (long groupId : SyncGroups) {
                Group syncGroup = MainEnv.BOT.getGroup(groupId);
                syncGroup.sendMessage(Config.JoinServerMessage.replace("%NAME%", event.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (needSync()) {
            for (long groupId : SyncGroups) {
                Group syncGroup = MainEnv.BOT.getGroup(groupId);
                syncGroup.sendMessage(Config.LeaveServerMessage.replace("%NAME%", event.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (needSync()) {
            for (long groupId : SyncGroups) {
                Group syncGroup = MainEnv.BOT.getGroup(groupId);
                syncGroup.sendMessage(Config.SayServerMessage.replace("%NAME%", event.getPlayer().getName()).replace("%MESSAGE%", event.getMessage()));
            }
        }
    }

    private boolean needSync() {
        return Config.SyncChatEnabled & !Config.SyncChatEnabledQ2SOnly & !Config.BotModeOfficial;
    }

    public void setModuleName() {
        if (!Config.QQRobotEnabled) {
            this.moduleName = null;
        }
    }
}
