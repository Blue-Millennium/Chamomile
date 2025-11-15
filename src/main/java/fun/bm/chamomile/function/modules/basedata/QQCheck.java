package fun.bm.chamomile.function.modules.basedata;

import fun.bm.chamomile.config.modules.Bot.AuthConfig;
import fun.bm.chamomile.config.modules.Bot.CoreConfig;
import fun.bm.chamomile.data.basedata.BaseData;
import fun.bm.chamomile.data.basedata.link.LinkData;
import fun.bm.chamomile.data.basedata.link.QQLinkData;
import fun.bm.chamomile.data.basedata.link.UseridLinkData;
import fun.bm.chamomile.data.basedata.player.PlayerData;
import fun.bm.chamomile.util.Environment;
import fun.bm.chamomile.util.TimeUtil;
import fun.bm.chamomile.util.helper.MainThreadHelper;
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

import static fun.bm.chamomile.util.Environment.LOGGER;

public class QQCheck extends BaseDataProcessor {
    private static final HashMap<PlayerData, Integer> playerCodeMap = new HashMap<>();

    public QQCheck() {
        super("QQCheck");
    }

    public static void groupCheck(GroupMessageEvent event, MessageChainBuilder builder) {
        String num = builder.build().contentToString().replace(" ", "").replace(AuthConfig.prefix.replace(" ", ""), "");
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
        List<BaseData> dataList = CoreConfig.official ? Environment.dataManager.baseDataManager.getPlayersByQQ(event.getGroup().getId()) : Environment.dataManager.baseDataManager.getPlayersByUserID(event.getGroup().getId());
        if (!dataList.isEmpty()) {
            for (BaseData data : dataList) {
                if (!data.linkData.isEmpty()
                        && data.linkData.stream()
                        .anyMatch(linkData ->
                                (!CoreConfig.official && linkData instanceof QQLinkData
                                        && ((QQLinkData) linkData).qqNumber == event.getGroup().getId())
                                        || (CoreConfig.official && linkData instanceof UseridLinkData
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
                BaseData data = new BaseData();
                for (BaseData data1 : Environment.dataManager.baseDataManager.DATA_LIST) {
                    if (data1.playerData.playerUuid.equals(entry.getKey().playerUuid)) {
                        data = data1;
                        flag = true;
                        break;
                    }
                }
                if (CoreConfig.official) {
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
                    Environment.dataManager.baseDataManager.setPlayerData(data.playerData.playerUuid, data, true);
                } else {
                    data.playerData = entry.getKey();
                    Environment.dataManager.baseDataManager.setPlayerDataByName(data.playerData.playerName, data, true);
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
        if (CoreConfig.official) {
            checkMessage.append("Linked UserID: ").append(String.valueOf(event.getSender().getId())).append("\n")
                    .append("Linked Time: ").append(TimeUtil.getNowTime());
        } else {
            checkMessage.append("Linked QQ: ").append(String.valueOf(event.getSender().getId())).append("\n")
                    .append("Linked Time: ").append(TimeUtil.getNowTime());
        }
        checkMessage.append("\n-----------------\n");
        return checkMessage;
    }

    public static int generateCode(BaseData data) {
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
        MainThreadHelper.botFuture.thenRun(() -> {
            if (!CoreConfig.official)
                Environment.eventChannel.subscribeAlways(NewFriendRequestEvent.class, NewFriendRequestEvent::accept);

            Environment.eventChannel.subscribeAlways(FriendMessageEvent.class, event -> {
                String message = event.getMessage().contentToString();

                int code;
                try {
                    code = Integer.parseInt(message);
                } catch (Exception exception) {
                    return;
                }
                event.getSender().sendMessage(dataCheck(event, code));
            });
        });
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event == null) {
            return;
        }

        BaseData data = Environment.dataManager.baseDataManager.getPlayerData(event.getUniqueId());
        data = Environment.dataManager.baseDataManager.nullCheck(data);
        data.playerData.playerUuid = event.getUniqueId();
        data.playerData.playerName = event.getName();
        // 首次登录
        int code = generateCode(data);
        if (AuthConfig.enforceCheck && data.linkData.isEmpty()) {
            // 拒绝加入服务器
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, AuthConfig.disconnectMessage.replace("%CODE%", String.valueOf(code)));
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
        if (!AuthConfig.enforceCheck) {
            try {
                Player player = event.getPlayer();
                int code = -1;
                for (Map.Entry<PlayerData, Integer> entry : playerCodeMap.entrySet()) {
                    if (entry.getKey().playerUuid.equals(player.getUniqueId())) {
                        code = entry.getValue();
                    }
                }
                BaseData data = Environment.dataManager.baseDataManager.getPlayerData(event.getPlayer().getUniqueId());
                if (!data.linkData.isEmpty()) {
                    player.sendMessage(AuthConfig.connectMessage);
                } else {
                    if (code == -1) {
                        code = generateCode(data);
                    }
                    player.sendMessage(AuthConfig.disconnectMessage.replace("%CODE%", String.valueOf(code)));
                }
            } catch (Exception e) {
                LOGGER.warning(e.getMessage());
            }
        }
    }

    public void setModuleName() {
        if (!CoreConfig.enabled || !AuthConfig.enabled) {
            this.moduleName = null;
        }
    }
}
