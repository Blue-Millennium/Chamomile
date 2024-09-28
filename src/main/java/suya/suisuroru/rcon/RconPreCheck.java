package suya.suisuroru.rcon;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.bukkit.event.Listener;
import xd.suka.Main;
import xd.suka.config.Config;
import xd.suka.module.Module;

import java.util.ArrayList;
import java.util.List;

import static suya.suisuroru.rcon.RconCommandExecute.executeRconCommand;
import static xd.suka.Main.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/9/28 16:06p.m.
 * function: Check message if need rcon function
 */
public class RconPreCheck extends Module implements Listener {
    private List<Long> EnabledGroups = new ArrayList<>();

    public RconPreCheck() {
        super("DirectConsoleCommandCheck");
    }

    @Override
    public void onEnable() {
        // 解析 Config.DirectConsoleEnabledGroups 字符串
        String enabledGroupStr = Config.RconEnabledGroups;
        if (enabledGroupStr == null || enabledGroupStr.isEmpty()) {
            LOGGER.warning("[RCONCommandCheck] RCON commands will be disabled due to empty or null RCONEnabledGroups");
            Config.RconEnabled = false;
            Main.INSTANCE.configManager.save();
            return;
        }

        String[] groupIds = enabledGroupStr.split(";");
        for (String groupId : groupIds) {
            try {
                long id = Long.parseLong(groupId.trim());
                EnabledGroups.add(id);
            } catch (NumberFormatException e) {
                LOGGER.warning("[RCONCommandCheck] Invalid group ID: " + groupId);
            }
        }

        if (EnabledGroups.isEmpty()) {
            LOGGER.warning("[RCONCommandCheck] RCON commands will be disabled");
            Config.RconEnabled = false;
            Main.INSTANCE.configManager.save();
            return;
        }

        Main.INSTANCE.eventChannel.subscribeAlways(GroupMessageEvent.class, event -> {
            if (!Config.RconEnabled || !EnabledGroups.contains(event.getGroup().getId())) {
                return;
            }

            Message message = event.getMessage();
            String content = message.contentToString();

            if (content.startsWith(Config.ExecuteCommandPrefix)) {
                int prefixLength = Config.ExecuteCommandPrefix.length();
                String command = content.substring(prefixLength);
                String result = executeRconCommand(command);
                handleConsoleResult(result, event);
            }
        });
    }

    private void handleConsoleResult(String result, GroupMessageEvent event) {
        if (result != null && !result.isEmpty()) {
            event.getGroup().sendMessage(new MessageChainBuilder()
                    .append(new PlainText("Console command result: \n" + result))
                    .build());
        } else {
            event.getGroup().sendMessage(new MessageChainBuilder()
                    .append(new PlainText("No result from console command."))
                    .build());
        }
    }

}
