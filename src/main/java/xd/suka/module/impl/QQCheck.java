package xd.suka.module.impl;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import xd.suka.Main;
import xd.suka.config.Config;
import xd.suka.data.Data;
import xd.suka.data.PlayerData;
import xd.suka.module.Module;
import xd.suka.util.TimeUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xd.suka.Main.LOGGER;

public class QQCheck extends Module implements Listener {
    private final Map<PlayerData, Integer> playerCodeMap = new HashMap<>();

    public QQCheck() {
        super("QQCheck");
    }

    private static boolean isPureCode(String str) {
        // 正则表达式，匹配只包含数字的字符串
        String regex = "^\\d$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    @Override
    public void onEnable() {
        Main.INSTANCE.eventChannel.subscribeAlways(NewFriendRequestEvent.class, NewFriendRequestEvent::accept);

        Main.INSTANCE.eventChannel.subscribeAlways(FriendMessageEvent.class, event -> {
            String message = event.getMessage().contentToString();

            if (Config.qqCheckEnabled) {
                // 判断是否为验证码格式
                if (!isPureCode(message)) {
                    return;
                }

                // 判断是否为有效验证码
                if (playerCodeMap.containsValue(Integer.parseInt(message))) {
                    PlayerData playerData = playerCodeMap.entrySet().stream().filter(entry -> entry.getValue().equals(Integer.parseInt(message))).map(Map.Entry::getKey).findFirst().orElse(null);
                    if (playerData != null) {
                        // 设置绑定数据
                        Data data = new Data();
                        data.playerData = playerData;
                        data.qqNumber = event.getSender().getId();
                        data.linkedTime = System.currentTimeMillis();
                        Main.INSTANCE.dataManager.DATA_LIST.add(data);
                        Main.INSTANCE.dataManager.save();

                        // 发送确认消息
                        MessageChain checkMessage = new MessageChainBuilder()
                                .append("Your account was linked!").append("\n")
                                .append("Player Name: ").append(playerData.playerName).append("\n")
                                .append("Linked QQ: ").append(String.valueOf(event.getSender().getId())).append("\n")
                                .append("Linked Time: ").append(TimeUtil.getNowTime())
                                .build();

                        event.getSender().sendMessage(checkMessage);
                    } else {
                        LOGGER.warning("Failed to find player data for verification code: " + message);
                    }
                } else {
                    event.getSender().sendMessage("Invalid verification code.");  // 验证码无效
                }
            }
        });
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (Config.qqCheckEnabled) {
            Data data = Main.INSTANCE.dataManager.getPlayerData(event.getUniqueId());
            // 首次登录
            if (data == null) {
                int code;

                // 生成不重复的验证码
                do {
                    // 在等待列表中移除重复的玩家uuid (玩家重新连接)
                    playerCodeMap.entrySet().stream().filter(entry -> entry.getKey().playerUuid.equals(event.getUniqueId())).forEach(entry -> playerCodeMap.remove(entry.getKey()));
                    code = Math.abs(new Random().nextInt(100000));
                } while (playerCodeMap.containsValue(code));

                // 加入等待列表
                PlayerData playerData = new PlayerData();
                playerData.playerName = event.getName();
                playerData.playerUuid = event.getUniqueId();
                playerCodeMap.put(playerData, code);

                // 拒绝加入服务器
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Config.disTitle.replace("%CODE%", String.valueOf(code)));
                return;
            }

            // 设置首次登陆数据
            if (data.firstJoin < 0) {
                data.firstJoin = System.currentTimeMillis();
            }
            if (data.firstJoinIp == null) {
                data.firstJoinIp = event.getAddress().getHostAddress();
            }

            data.lastJoin = System.currentTimeMillis();
            data.lastJoinIp = event.getAddress().getHostAddress();

            Main.INSTANCE.dataManager.setPlayerData(event.getUniqueId(), data);
        }
    }
}
