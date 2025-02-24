package fun.blue_millennium.module.impl;

import fun.blue_millennium.Chamomile;
import fun.blue_millennium.config.Config;
import fun.blue_millennium.data.AuthData.DataGet;
import fun.blue_millennium.data.Data;
import fun.blue_millennium.data.PlayerData;
import fun.blue_millennium.module.Module;
import fun.blue_millennium.util.TimeUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static fun.blue_millennium.Chamomile.LOGGER;
import static fun.blue_millennium.module.impl.DataProcess.BaseDataProcess;

public class QQCheck extends Module {
    private static final HashMap<PlayerData, Integer> playerCodeMap = new HashMap<>();

    public QQCheck() {
        super("QQCheck");
    }

    public static void GroupCheck(GroupMessageEvent event, MessageChainBuilder builder) {
        if (Config.QQCheckEnabled) {
            if (builder.build().contentToString().replace(" ", "").replace(Config.QQCheckStartWord, "").equals("test")) {
                MessageChainBuilder checkMessage;
                checkMessage = new MessageChainBuilder()
                        .append("Your account was linked!").append("\n")
                        .append("Player Name: ").append("test").append("\n")
                        .append("Linked UserID: ").append("ed0da36d-5cd6-4eb1-8fdb-33823927d2fc").append("\n")
                        .append("Linked Time: ").append(TimeUtil.getNowTime());
                event.getGroup().sendMessage(checkMessage.build());
            }
            boolean check_tag = false;
            DataGet dp = new DataGet();
            List<PlayerData> pdl = dp.getPlayerDataByUserID(event.getSender().getId());
            List<MessageChainBuilder> builderList = new java.util.ArrayList<>(List.of());
            for (PlayerData pd : pdl) {
                if (Bukkit.getServer().getOperators().contains(Bukkit.getPlayer(pd.playerUuid))) {
                    check_tag = true;
                    builderList.add(BuildMessage(event, pd.playerName));
                }
            }
            if (check_tag) {
                MessageChainBuilder finalBuilder = new MessageChainBuilder();
                for (MessageChainBuilder txt : builderList) {
                    finalBuilder.add("-----------------\n");
                    finalBuilder.add(txt.build());
                }
                finalBuilder.add("-----------------\n");
                event.getGroup().sendMessage(finalBuilder.build());
            } else {
                int code;
                try {
                    code = Integer.parseInt(builder.build().contentToString().replace(" ", "").replace(Config.QQCheckStartWord, ""));
                } catch (Exception exception) {
                    return;
                }
                event.getGroup().sendMessage(DataCheck(event, code));
            }
        }
    }

    private static MessageChain DataCheck(MessageEvent event, int code) {
        MessageChainBuilder checkMessage = null;
        for (Map.Entry<PlayerData, Integer> entry : playerCodeMap.entrySet()) {
            if (entry.getValue().equals(code)) {
                playerCodeMap.remove(entry.getKey());

                // 保存数据
                Data data = new Data();
                data.playerData = entry.getKey();
                if (Config.BotModeOfficial) {
                    data.userid = event.getSender().getId();
                    data.useridLinkedTime = System.currentTimeMillis();
                } else {
                    data.qqNumber = event.getSender().getId();
                    data.linkedTime = System.currentTimeMillis();
                }
                Chamomile.INSTANCE.dataManager.DATA_LIST.add(data);
                Chamomile.INSTANCE.dataManager.save();

                // 构建确认消息
                checkMessage = BuildMessage(event, entry.getKey().playerName);
            }
        }
        return checkMessage.build();
    }

    @NotNull
    private static MessageChainBuilder BuildMessage(MessageEvent event, String playerName) {
        MessageChainBuilder checkMessage;
        checkMessage = new MessageChainBuilder()
                .append("Your account was linked!").append("\n")
                .append("Player Name: ").append(playerName).append("\n");
        if (Config.BotModeOfficial) {
            checkMessage.append("Linked UserID: ").append(String.valueOf(event.getSender().getId())).append("\n")
                    .append("Linked Time: ").append(TimeUtil.getNowTime());
        } else {
            checkMessage.append("Linked QQ: ").append(String.valueOf(event.getSender().getId())).append("\n")
                    .append("Linked Time: ").append(TimeUtil.getNowTime());
        }
        return checkMessage;
    }

    public static Data NullCheck(Data data) {
        if (data == null) {
            data = new Data();
        }
        return data;
    }

    @Override
    public void onEnable() {
        Chamomile.eventChannel.subscribeAlways(NewFriendRequestEvent.class, NewFriendRequestEvent::accept);

        Chamomile.eventChannel.subscribeAlways(FriendMessageEvent.class, event -> {
            String message = event.getMessage().contentToString();

            if (Config.QQCheckEnabled) {
                int code;
                try {
                    code = Integer.parseInt(message);
                } catch (Exception exception) {
                    return;
                }
                event.getSender().sendMessage(DataCheck(event, code));
            }
        });
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event == null) {
            return;
        }

        if (Config.QQCheckEnabled) {
            Data data = Chamomile.INSTANCE.dataManager.getPlayerData(event.getUniqueId());
            data = NullCheck(data);
            // 首次登录
            if (data.qqNumber == 0 || data.userid == 0) {
                int code;

                // 生成不重复的验证码
                do {
                    for (Map.Entry<PlayerData, Integer> entry : playerCodeMap.entrySet()) {
                        if (entry.getKey().playerUuid.equals(event.getUniqueId())) {
                            playerCodeMap.remove(entry.getKey());
                        }
                    }
                    code = Math.abs(new Random().nextInt(100000));
                } while (playerCodeMap.containsValue(code));

                // 加入等待列表
                PlayerData playerData = new PlayerData();
                playerData.playerName = event.getName();
                playerData.playerUuid = event.getUniqueId();
                playerCodeMap.put(playerData, code);

                if (Config.EnforceCheckEnabled) {
                    // 拒绝加入服务器
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Config.DisTitle.replace("%CODE%", String.valueOf(code)));
                } else {
                    LOGGER.info(Config.DisTitle.replace("%CODE%", String.valueOf(code)));
                }
                return;
            } else {
                if (Config.BotModeOfficial) {
                    data.useridChecked = true;
                } else {
                    data.qqChecked = true;
                }
            }

            // 设置首次登陆数据
            BaseDataProcess(event, data);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            Player player = event.getPlayer();
            int code = -1;
            for (Map.Entry<PlayerData, Integer> entry : playerCodeMap.entrySet()) {
                if (entry.getKey().playerUuid.equals(player.getUniqueId())) {
                    code = entry.getValue();
                }
            }
            if (code != -1) {
                player.sendMessage(Config.DisTitle.replace("%CODE%", String.valueOf(code)));
            }
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
    }
}
