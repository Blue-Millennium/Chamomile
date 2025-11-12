package fun.bm.chamomile.function.modules;

import fun.bm.chamomile.config.modules.Bot.AuthConfig;
import fun.bm.chamomile.config.modules.Bot.CoreConfig;
import fun.bm.chamomile.config.modules.Bot.RconConfig;
import fun.bm.chamomile.config.modules.Bot.SyncConfig;
import fun.bm.chamomile.data.basedata.BaseData;
import fun.bm.chamomile.data.basedata.link.UseridLinkData;
import fun.bm.chamomile.function.Function;
import fun.bm.chamomile.function.modules.data.QQCheck;
import fun.bm.chamomile.util.Environment;
import fun.bm.chamomile.util.helper.MainThreadHelper;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.chamomile.util.Environment.LOGGER;
import static fun.bm.chamomile.util.ImageProcessor.*;
import static fun.bm.chamomile.util.helper.RconHelper.executeRconCommand;

public class SyncChat extends Function {
    public static final List<Long> SyncGroups = new ArrayList<>();

    public SyncChat() {
        super("SyncChat");
    }

    public void onEnable() {
        MainThreadHelper.botFuture.thenRun(() -> {
            String[] groupIds = SyncConfig.groups.split(";");
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

            if (!CoreConfig.official) {
                if (SyncGroups.isEmpty()) {
                    LOGGER.warning("Failed to get sync group");
                    Environment.configManager.setConfigAndSave("bot.sync-chat.enabled", false);
                    return;
                }
            }

            Environment.eventChannel.subscribeAlways(GroupMessageEvent.class, event -> {
                for (long groupId : SyncGroups) {
                    Group syncGroup = Environment.BOT.getGroup(groupId);
                    if (!CoreConfig.official & (!SyncConfig.enabled || event.getGroup() != syncGroup)) {
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
                    if (AuthConfig.enabled && builder.build().contentToString().replace(" ", "").startsWith(AuthConfig.prefix.replace(" ", ""))) {
                        QQCheck.groupCheck(event, builder);
                    } else if (CoreConfig.official && builder.build().contentToString().replace(" ", "").startsWith(SyncConfig.prefix.replace(" ", ""))) {
                        while (builder.build().contentToString().startsWith(" ")) builder.remove(0);
                        String avatar = "QQ用户" + processImageUrl(event.getSender().getAvatarUrl());
                        String id = getID(event);
                        String message = SyncConfig.qqMessage.replace("%NAME%", avatar + id + "发送了以下消息").replace("%MESSAGE%", builder.build().contentToString().replace(SyncConfig.prefix, ""));
                        sendMessage(message);
                        event.getGroup().sendMessage("已成功发送消息至服务器，以下为发送至服务器的原始数据：\n" + replaceUrl(message));
                    } else if (CoreConfig.official) {
                        sendMessage(SyncConfig.qqMessage.replace("%NAME%", event.getSenderName()).replace("%MESSAGE%", builder.build().contentToString()));
                    }
                }
            });
        });
    }

    public void sendMessage(String message) {
        String command = "tellraw @a \"" + message + "\"";
        executeRconCommand(RconConfig.ip, RconConfig.port, RconConfig.password, command);
    }

    public String getID(GroupMessageEvent event) {
        List<String> pd = new ArrayList<>();
        List<BaseData> dataList = Environment.dataManager.baseDataManager.getPlayersByUserID(event.getGroup().getId());
        if (!dataList.isEmpty()) {
            for (BaseData data : dataList) {
                if (!data.linkData.isEmpty()
                        && data.linkData.stream()
                        .anyMatch(linkData -> (CoreConfig.official && linkData instanceof UseridLinkData
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
                Group syncGroup = Environment.BOT.getGroup(groupId);
                syncGroup.sendMessage(SyncConfig.joinMessage.replace("%NAME%", event.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (needSync()) {
            for (long groupId : SyncGroups) {
                Group syncGroup = Environment.BOT.getGroup(groupId);
                syncGroup.sendMessage(SyncConfig.leaveMessage.replace("%NAME%", event.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (needSync()) {
            for (long groupId : SyncGroups) {
                Group syncGroup = Environment.BOT.getGroup(groupId);
                syncGroup.sendMessage(SyncConfig.serverMessage.replace("%NAME%", event.getPlayer().getName()).replace("%MESSAGE%", event.getMessage()));
            }
        }
    }

    private boolean needSync() {
        return SyncConfig.enabled && !SyncConfig.qqToServerOnly && !CoreConfig.official && MainThreadHelper.isBotRunning();
    }

    public void setModuleName() {
        if (!CoreConfig.enabled) {
            this.moduleName = null;
        }
    }
}
