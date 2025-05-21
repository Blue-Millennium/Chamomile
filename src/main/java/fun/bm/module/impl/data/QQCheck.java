package fun.bm.module.impl.data;

import fun.bm.config.old.Config;
import fun.bm.data.manager.data.Data;
import fun.bm.data.manager.data.link.LinkData;
import fun.bm.data.manager.data.link.QQLinkData;
import fun.bm.data.manager.data.link.UseridLinkData;
import fun.bm.data.manager.data.player.PlayerData;
import fun.bm.module.Module;
import fun.bm.util.MainEnv;
import fun.bm.util.TimeUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static fun.bm.command.main.executor.extra.sub.data.Query.dataGet;
import static fun.bm.module.impl.data.DataProcess.baseDataProcess;
import static fun.bm.util.MainEnv.LOGGER;

public class QQCheck extends Module {
    private static final HashMap<PlayerData, Integer> playerCodeMap = new HashMap<>();

    public QQCheck() {
        super("QQCheck");
    }

    public static void groupCheck(GroupMessageEvent event, MessageChainBuilder builder) {
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
                event.getGroup().sendMessage(dataCheck(event, code));
                return;
            }
        } catch (Exception ignored) {
        }
        List<MessageChainBuilder> builderList = new java.util.ArrayList<>();
        List<Data> dataList = Config.BotModeOfficial ? dataGet.getPlayersByQQ(event.getGroup().getId()) : dataGet.getPlayersByUserID(event.getGroup().getId());
        if (!dataList.isEmpty()) {
            for (Data data : dataList) {
                if (!data.linkData.isEmpty()
                        && data.linkData.stream()
                        .anyMatch(linkData ->
                                (!Config.BotModeOfficial && linkData instanceof QQLinkData
                                        && ((QQLinkData) linkData).qqNumber == event.getGroup().getId())
                                        || (Config.BotModeOfficial && linkData instanceof UseridLinkData
                                        && ((UseridLinkData) linkData).userid == event.getSender().getId())))
                    builderList.add(buildMessage(event, data.playerData.playerName));
            }
        }
        if (!builderList.isEmpty()) {
            MessageChainBuilder finalBuilder = new MessageChainBuilder();
            for (MessageChainBuilder txt : builderList) {
                finalBuilder.add("\n-----------------\n");
                finalBuilder.add(txt.build());
            }
            finalBuilder.add("\n-----------------\n");
            event.getGroup().sendMessage(finalBuilder.build());
        }
    }

    private static MessageChain dataCheck(MessageEvent event, int code) {
        MessageChainBuilder checkMessage = null;
        for (Map.Entry<PlayerData, Integer> entry : playerCodeMap.entrySet()) {
            if (entry.getValue().equals(code)) {
                playerCodeMap.remove(entry.getKey());

                // 保存数据
                boolean flag = false;
                Data data = new Data();
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
                    data.useridLinkedTime = TimeUtil.getUnixTimeMs();
                } else {
                    data.qqChecked = true;
                    data.qqNumber = event.getSender().getId();
                    data.linkedTime = TimeUtil.getUnixTimeMs();
                }
                if (flag) {
                    MainEnv.dataManager.setPlayerData(data.playerData.playerUuid, data);
                } else {
                    data.playerData = entry.getKey();
                    MainEnv.dataManager.setPlayerDataByName(data.playerData.playerName, data);
                    MainEnv.dataManager.save();
                }

                // 构建确认消息
                checkMessage = buildMessage(event, data.playerData.playerName);
            }
        }
        return checkMessage.build();
    }

    @NotNull
    private static MessageChainBuilder buildMessage(MessageEvent event, String playerName) {
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

    public static int generateCode(Data data) {
        int code;

        // 生成不重复的验证码
        do {
            for (Map.Entry<PlayerData, Integer> entry : playerCodeMap.entrySet()) {
                if (entry.getKey().playerUuid.equals(data.playerData.playerUuid)) {
                    playerCodeMap.remove(entry.getKey());
                }
            }
            code = Math.abs(new Random().nextInt(100000));
        } while (playerCodeMap.containsValue(code));

        // 加入等待列表
        PlayerData playerData = data.playerData;
        playerCodeMap.put(playerData, code);
        return code;
    }

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
            event.getSender().sendMessage(dataCheck(event, code));
        });
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event == null) {
            return;
        }

        Data data = MainEnv.dataManager.getPlayerData(event.getUniqueId());
        data = MainEnv.dataManager.nullCheck(data);
        data.playerData.playerUuid = event.getUniqueId();
        data.playerData.playerName = event.getName();
        // 首次登录
        int code = generateCode(data);
        if (Config.EnforceCheckEnabled && data.linkData.isEmpty()) {
            // 拒绝加入服务器
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Config.DisTitle.replace("%CODE%", String.valueOf(code)));
        } else {
            for (LinkData linkData : data.linkData) {
                if (data.qqChecked && data.useridChecked) break;
                if (linkData instanceof QQLinkData) data.qqChecked = true;
                if (linkData instanceof UseridLinkData) data.useridChecked = true;
            }
        }

        // 设置首次登陆数据
        baseDataProcess(event, data);
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
                if (code != -1 && data.linkData.isEmpty()) {
                    player.sendMessage(Config.DisTitle.replace("%CODE%", String.valueOf(code)));
                } else {
                    player.sendMessage(Config.ConnTitle);
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
