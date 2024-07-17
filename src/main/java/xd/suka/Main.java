package xd.suka;

import com.google.gson.Gson;
import net.kyori.adventure.text.Component;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.mrxiaom.overflow.BotBuilder;
import xd.suka.data.Data;
import xd.suka.data.DataManager;
import xd.suka.data.PlayerData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Liycxc
 * Date: 2024/7/16 下午8:38
 */
public class Main extends JavaPlugin implements Listener {
    public static Logger logger = LoggerFactory.getLogger("BaseLogger");
    public static Bot BOT;
    public DataManager dataManager;
    public GlobalEventChannel eventChannel;
    private final Map<PlayerData, Integer> playerCodeMap = new HashMap<>();

    @Override
    public void onEnable() {
        dataManager = new DataManager();
        dataManager.install();

        BOT = BotBuilder.positive("ws://192.168.2.39:5800").token("114514").connect();
        eventChannel = GlobalEventChannel.INSTANCE;
        Bukkit.getPluginManager().registerEvents(this, this);

        eventChannel.subscribeAlways(NewFriendRequestEvent.class, NewFriendRequestEvent::accept);
        eventChannel.subscribeAlways(FriendMessageEvent.class, event -> {
            String message = event.getMessage().contentToString();
            if (playerCodeMap.containsValue(Integer.parseInt(message))) {
                PlayerData playerData = playerCodeMap.entrySet().stream().filter(entry -> entry.getValue().equals(Integer.parseInt(message))).map(Map.Entry::getKey).findFirst().orElse(null);
                if (playerData != null) {
                    Data data = new Data();
                    data.PLAYER_DATA = playerData;
                    data.QQ_ID = event.getSender().getId();
                    data.LINKED_TIME = System.currentTimeMillis();
                    dataManager.dataList.add(data);
                    dataManager.saveAll();

                    LocalDateTime currentTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedTime = currentTime.format(formatter);

                    MessageChain checkMessage = new MessageChainBuilder()
                            .append("Your account was linked!").append("\n")
                            .append("Player Name: ").append(playerData.PLAYER_NAME).append("\n")
                            .append("Player UUID: ").append(playerData.PLAYER_UUID.toString()).append("\n")
                            .append("Linked QQ: ").append(String.valueOf(event.getSender().getId())).append("\n")
                            .append("Linked Time: ").append(formattedTime)
                            .build();

                    event.getSender().sendMessage(checkMessage);
                }
            }
        });
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        Data data = dataManager.getPlayerData(event.getUniqueId());
        if (data == null) {
            int code;
            do {
                playerCodeMap.entrySet().stream().filter(entry -> entry.getKey().PLAYER_UUID.equals(event.getUniqueId())).forEach(entry -> playerCodeMap.remove(entry.getKey()));
                code = Math.abs(new Random().nextInt(100000));
            } while (playerCodeMap.containsValue(code));

            PlayerData playerData = new PlayerData();
            playerData.PLAYER_NAME = event.getName();
            playerData.PLAYER_UUID = event.getUniqueId();
            playerCodeMap.put(playerData, code);
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("Your code is " + code));
            return;
        }

        if (data.FIRST_JOIN < 0) {
            data.FIRST_JOIN = System.currentTimeMillis();
        }
        data.LAST_JOIN = System.currentTimeMillis();
        dataManager.setPlayerData(event.getUniqueId(), data);

        Gson gson = new Gson();
        System.out.println(gson.toJson(data));
    }
}
