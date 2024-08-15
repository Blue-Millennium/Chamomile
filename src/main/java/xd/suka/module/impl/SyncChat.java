package xd.suka.module.impl;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xd.suka.Main;
import xd.suka.config.Config;
import xd.suka.module.Module;

import static xd.suka.Main.LOGGER;

public class SyncChat extends Module implements Listener {
    private Group syncGroup = null;

    public SyncChat() {
        super("SyncChat");
    }

    @Override
    public void onEnable() {
        syncGroup = Main.INSTANCE.BOT.getGroup(Config.syncChatGroup);

        if (syncGroup == null) {
            LOGGER.warning("Failed to get sync group");
            Config.syncChatEnabled = false;
            Main.INSTANCE.configManager.save();
            return;
        }

        Main.INSTANCE.eventChannel.subscribeAlways(GroupMessageEvent.class, event -> {
            if (!Config.syncChatEnabled || event.getGroup() != syncGroup) {
                return;
            }

            MessageChainBuilder builder = new MessageChainBuilder();
            for (Message message : event.getMessage()) {
                if (message instanceof PlainText) {
                    builder.add(message);
                }
            }

            if (!builder.isEmpty()) {
                Main.INSTANCE.getServer().broadcastMessage(Config.sayQQMessage.replace("%NAME%", event.getSenderName()).replace("%MESSAGE%", builder.build().contentToString()));
            }
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Config.syncChatEnabled) {
            syncGroup.sendMessage(Config.joinServerMessage.replace("%NAME%", event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (Config.syncChatEnabled) {
            syncGroup.sendMessage(Config.leaveServerMessage.replace("%NAME%", event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (Config.syncChatEnabled) {
            syncGroup.sendMessage(Config.sayServerMessage.replace("%NAME%", event.getPlayer().getName()).replace("%MESSAGE%", event.getMessage()));
        }
    }
}
