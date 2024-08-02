package xd.suka;

import net.kyori.adventure.text.Component;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.LoggerAdapters;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.mrxiaom.overflow.BotBuilder;
import xd.suka.config.Config;
import xd.suka.config.ConfigManager;
import xd.suka.data.Data;
import xd.suka.data.DataManager;
import xd.suka.data.PlayerData;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Liycxc
 * Date: 2024/7/16 下午8:38
 */
public class Main extends JavaPlugin implements Listener {
    public static final Logger LOGGER = LoggerFactory.getLogger("QQLogin");
    public static File BASE_DIR = new File("QQLogin");
    public static File DATA_FILE = new File(BASE_DIR, "data.json");
    public static File CONFIG_FILE = new File(BASE_DIR, "config.properties");
    private final Map<PlayerData, Integer> playerCodeMap = new HashMap<>();

    public DataManager dataManager;
    public ConfigManager configManager;
    public GlobalEventChannel eventChannel;
    public Bot BOT;

    public static boolean isPureCode(String str) {
        // 正则表达式，匹配只包含数字的字符串，并且是五位数
        String regex = "^\\d{5}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    @Override
    public void onLoad() {
        dataManager = new DataManager();
        configManager = new ConfigManager();

        if (!BASE_DIR.exists()) {
            if (!BASE_DIR.mkdir()) {
                LOGGER.error("Failed to create directory: {}", BASE_DIR.getAbsolutePath());
            }
        }
        if (!DATA_FILE.exists()) {
            try {
                if (!DATA_FILE.createNewFile()) {
                    LOGGER.error("Failed to create data file");
                }
            } catch (Exception exception) {
                LOGGER.error("Failed to create data file", exception);
            }
        }

        dataManager.load();
        configManager.load();
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this); // 注册事件

        LoggerAdapters.useLog4j2(); // 使用 Log4j2 作为日志记录器
        BOT = BotBuilder.positive(Config.botWsUrl).token(Config.botWsToken).connect(); // 连接 LLOneBot
        eventChannel = GlobalEventChannel.INSTANCE;

        // 订阅好友请求事件
        eventChannel.subscribeAlways(NewFriendRequestEvent.class, NewFriendRequestEvent::accept);

        // 订阅好友消息事件
        eventChannel.subscribeAlways(FriendMessageEvent.class, event -> {
            String message = event.getMessage().contentToString();

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
                    dataManager.DATA_LIST.add(data);
                    dataManager.save();

                    LocalDateTime currentTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedTime = currentTime.format(formatter);

                    // 发送确认消息
                    MessageChain checkMessage = new MessageChainBuilder()
                            .append("Your account was linked!").append("\n")
                            .append("Player Name: ").append(playerData.playerName).append("\n")
                            .append("Linked QQ: ").append(String.valueOf(event.getSender().getId())).append("\n")
                            .append("Linked Time: ").append(formattedTime)
                            .build();

                    event.getSender().sendMessage(checkMessage);
                } else {
                    LOGGER.error("Failed to find player data for verification code: {}", message);
                }
            } else {
                event.getSender().sendMessage("Invalid verification code."); // 验证码无效
            }
        });
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        Data data = dataManager.getPlayerData(event.getUniqueId());
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
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text(String.format(Config.disTitle, code)));
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

        dataManager.setPlayerData(event.getUniqueId(), data);
    }
}
