package fun.bm.module.impl.PlayerDataProcess;

import fun.bm.config.Config;
import fun.bm.data.PlayerData.Data;
import fun.bm.data.PlayerData.PlayerData;
import fun.bm.module.Module;
import fun.bm.util.MainEnv;
import fun.bm.util.TimeUtil;
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

import static fun.bm.module.impl.PlayerDataProcess.DataProcess.BaseDataProcess;
import static fun.bm.util.MainEnv.LOGGER;

public class QQCheck extends Module {
    private static final HashMap<PlayerData, Integer> playerCodeMap = new HashMap<>();

    public QQCheck() {
        super("QQCheck");
    }

    public static void GroupCheck(GroupMessageEvent event, MessageChainBuilder builder) {
        String num = builder.build().contentToString().replace(" ", "").replace(Config.QQCheckStartWord.replace(" ", ""), "");
        int code;
        if (num.equals("test")) {
            MessageChainBuilder checkMessage;
            checkMessage = new MessageChainBuilder()
                    .append("\n-----------------\n")
                    .append("Your account was linked!").append("\n")
                    .append("Player Name: ").append("test").append("\n")
                    .append("Linked UserID: ").append("ed0da36d-5cd6-4eb1-8fdb-33823927d2fc").append("\n")
                    .append("Linked Time: ").append(TimeUtil.getNowTime())
                    .append("\n-----------------\n");
            event.getGroup().sendMessage(checkMessage.build());
            return;
        }
        try {
            code = Integer.parseInt(num);
            if (code != 0) {
                event.getGroup().sendMessage(DataCheck(event, code));
                return;
            }
        } catch (Exception ignored) {
        }
        boolean check_tag = false;
        List<MessageChainBuilder> builderList = new java.util.ArrayList<>(List.of());
        for (Data data : MainEnv.dataManager.DATA_LIST) {
            if (Bukkit.getServer().getOperators().contains(Bukkit.getPlayer(data.playerData.playerUuid))
                    && data.useridLinkedGroup == event.getGroup().getId()) {
                check_tag = true;
                builderList.add(BuildMessage(event, data.playerData.playerName));
            }
        }
        if (check_tag) {
            MessageChainBuilder finalBuilder = new MessageChainBuilder();
            for (MessageChainBuilder txt : builderList) {
                finalBuilder.add("\n-----------------\n");
                finalBuilder.add(txt.build());
            }
            finalBuilder.add("\n-----------------\n");
            event.getGroup().sendMessage(finalBuilder.build());
        }
    }

    private static MessageChain DataCheck(MessageEvent event, int code) {
        MessageChainBuilder checkMessage = null;
        for (Map.Entry<PlayerData, Integer> entry : playerCodeMap.entrySet()) {
            if (entry.getValue().equals(code)) {
                playerCodeMap.remove(entry.getKey());

                // 保存数据
                Data data;
                boolean flag = false;
                data = new Data();
                for (Data data1 : MainEnv.dataManager.DATA_LIST) {
                    if (data1.playerData.playerUuid.equals(entry.getKey().playerUuid)) {
                        data = data1;
                        flag = true;
                        break;
                    }
                }
                if (Config.BotModeOfficial) {
                    data.useridChecked = true;
                    if (event instanceof GroupMessageEvent)
                        data.useridLinkedGroup = ((GroupMessageEvent) event).getGroup().getId();
                    data.userid = event.getSender().getId();
                    data.useridLinkedTime = System.currentTimeMillis();
                } else {
                    data.qqChecked = true;
                    data.qqNumber = event.getSender().getId();
                    data.linkedTime = System.currentTimeMillis();
                }
                if (flag) {
                    MainEnv.dataManager.setPlayerData(data.playerData.playerUuid, data);
                } else {
                    data.playerData = entry.getKey();
                    MainEnv.dataManager.DATA_LIST.add(data);
                    MainEnv.dataManager.save();
                }

                // 构建确认消息
                checkMessage = BuildMessage(event, data.playerData.playerName);
            }
        }
        return checkMessage.build();
    }

    @NotNull
    private static MessageChainBuilder BuildMessage(MessageEvent event, String playerName) {
        MessageChainBuilder checkMessage;
        checkMessage = new MessageChainBuilder()
                .append("\n-----------------\n")
                .append("Your account was linked!").append("\n")
                .append("Player Name: ").append(playerName).append("\n");
        if (Config.BotModeOfficial) {
            checkMessage.append("Linked UserID: ").append(String.valueOf(event.getSender().getId())).append("\n")
                    .append("Linked Time: ").append(TimeUtil.getNowTime());
        } else {
            checkMessage.append("Linked QQ: ").append(String.valueOf(event.getSender().getId())).append("\n")
                    .append("Linked Time: ").append(TimeUtil.getNowTime());
        }
        checkMessage.append("\n-----------------\n");
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
        if (!Config.BotModeOfficial)
            MainEnv.eventChannel.subscribeAlways(NewFriendRequestEvent.class, NewFriendRequestEvent::accept);

        MainEnv.eventChannel.subscribeAlways(FriendMessageEvent.class, event -> {
            String message = event.getMessage().contentToString();

            int code;
            try {
                code = Integer.parseInt(message);
            } catch (Exception exception) {
                return;
            }
            event.getSender().sendMessage(DataCheck(event, code));
        });
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event == null) {
            return;
        }

        Data data = MainEnv.dataManager.getPlayerData(event.getUniqueId());
        data = NullCheck(data);
        // 首次登录
        if (data.qqNumber == 0 || data.userid == 0 || data.useridLinkedGroup == 0) {
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

            if (Config.EnforceCheckEnabled && data.qqNumber == 0 && data.userid == 0) {
                // 拒绝加入服务器
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Config.DisTitle.replace("%CODE%", String.valueOf(code)));
            } else if ((Config.BotModeOfficial && (data.userid == 0 || data.useridLinkedGroup == 0))
                    || (!Config.BotModeOfficial && data.qqNumber == 0)) {
                LOGGER.info(Config.DisTitle.replace("%CODE%", String.valueOf(code)));
                BaseDataProcess(event, data);
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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!Config.EnforceCheckEnabled) {
            try {
                Player player = event.getPlayer();
                int code = -1;
                for (Map.Entry<PlayerData, Integer> entry : playerCodeMap.entrySet()) {
                    if (entry.getKey().playerUuid.equals(player.getUniqueId())) {
                        code = entry.getValue();
                    }
                }
                Data data = MainEnv.dataManager.getPlayerData(event.getPlayer().getUniqueId());
                if (code != -1 && ((Config.BotModeOfficial && (data.userid == 0 || data.useridLinkedGroup == 0))
                        || (!Config.BotModeOfficial && data.qqNumber == 0))) {
                    player.sendMessage(Config.DisTitle.replace("%CODE%", String.valueOf(code)));
                }
            } catch (Exception e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }

    public void setModuleName() {
        if (!Config.QQRobotEnabled || !Config.QQCheckEnabled) {
            this.moduleName = null;
        }
    }
}
