package fun.bm.module.impl;

import fun.bm.config.Config;
import fun.bm.data.AuthData.DataGet;
import fun.bm.data.PlayerData.PlayerData;
import fun.bm.module.Module;
import fun.bm.util.MainEnv;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static fun.bm.data.Report.ReportCharmProcess.reportCharmProcess;
import static fun.bm.util.MainEnv.LOGGER;
import static fun.bm.util.helper.RconHelper.executeRconCommand;

/**
 * @author Suisuroru
 * Date: 2024/9/28 16:06
 * function: Check message if need rcon function
 */
public class ExecuteRcon extends Module {
    public static final List<Long> RconGroups = new ArrayList<>();

    public ExecuteRcon() {
        super("RCONCommandCheck");
    }

    @Override
    public void onEnable() {
        if (!Config.BotModeOfficial) {
            String enabledGroupStr = Config.RconEnabledGroups;
            if (enabledGroupStr == null || enabledGroupStr.isEmpty()) {
                LOGGER.warning("[RCONCommandCheck] RCON commands will be disabled due to empty or null RCONEnabledGroups");
                Config.RconEnabled = false;
                MainEnv.configManager.save();
                return;
            }

            String[] groupIds = enabledGroupStr.split(";");
            for (String groupId : groupIds) {
                try {
                    RconGroups.add(Long.parseLong(groupId.trim()));
                } catch (NumberFormatException e) {
                    LOGGER.warning("[RCONCommandCheck] Invalid group ID: " + groupId);
                }
            }

            if (RconGroups.isEmpty()) {
                LOGGER.warning("[RCONCommandCheck] RCON commands will be disabled");
                Config.RconEnabled = false;
                MainEnv.configManager.save();
                return;
            }
        }
        MainEnv.eventChannel.subscribeAlways(GroupMessageEvent.class, event -> {
            if (!Config.RconEnabled || !RconGroups.contains(event.getGroup().getId())) {
                return;
            }

            Message message = event.getMessage();
            String content = message.contentToString();

            if (content.startsWith(Config.ExecuteCommandPrefix)) {
                int prefixLength = Config.ExecuteCommandPrefix.length();
                String command = content.substring(prefixLength);
                boolean isOperator = false;
                if (!Config.BotModeOfficial) {
                    isOperator = event.getSender().getPermission().equals(MemberPermission.ADMINISTRATOR)
                            || event.getSender().getPermission().equals(MemberPermission.OWNER);
                } else {
                    DataGet dp = new DataGet();
                    List<PlayerData> PlayerDataList = dp.getPlayerDataByUserID(event.getSender().getId());
                    for (PlayerData pd : PlayerDataList) {
                        if (Bukkit.getServer().getOperators().contains(Bukkit.getPlayer(pd.playerUuid))) {
                            isOperator = true;
                            break;
                        }
                    }
                }
                if (Config.RconEnforceOperator) {
                    if (!isOperator) {
                        event.getGroup().sendMessage(new MessageChainBuilder()
                                .append(new PlainText("您没有权限执行此操作。"))
                                .build());
                        return;
                    }
                }
                String[] result = executeRconCommand(Config.RconIP, Config.RconPort, Config.RconPassword, command);
                handleConsoleResult(result, event);
            }
        });
    }

    private void handleConsoleResult(String[] result, GroupMessageEvent event) {
        try {
            if (result != null && !result[0].isEmpty()) {
                MessageChainBuilder message = new MessageChainBuilder();
                message.append(new PlainText(Config.ServerName + "Console command result: \n"))
                        .append(result[0]);
                String PREFIX = "[Chamomile Report]\n已查询到以下数据，下面的数据将按照以下顺序排列\n";
                if (!result[1].isEmpty() && result[1].startsWith(PREFIX)) {
                    reportCharmProcess(result[1].substring(PREFIX.length()));
                    message.append(PREFIX)
                            .append(event.getGroup().uploadImage(ExternalResource.create(new File(MainEnv.BASE_DIR, "CharmProcess\\latest.png"))));
                } else {
                    message.append(result[1]);
                }
                event.getGroup().sendMessage(message.build());
            } else {
                event.getGroup().sendMessage(new MessageChainBuilder()
                        .append(new PlainText(Config.ServerName + "No result from console command."))
                        .build());
            }
        } catch (Exception e) {
            event.getGroup().sendMessage(new MessageChainBuilder()
                    .append(new PlainText(Config.ServerName + "\n[ERROR] " + e.getMessage()))
                    .build());
        }
    }

    public void setModuleName() {
        if (!Config.QQRobotEnabled) {
            this.moduleName = null;
        }
    }
}
