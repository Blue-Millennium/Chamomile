package fun.bm.chamomile.function.modules;

import fun.bm.chamomile.config.modules.Bot.CoreConfig;
import fun.bm.chamomile.config.modules.Bot.RconConfig;
import fun.bm.chamomile.config.modules.ServerConfig;
import fun.bm.chamomile.data.manager.data.Data;
import fun.bm.chamomile.function.Function;
import fun.bm.chamomile.util.MainEnv;
import fun.bm.chamomile.util.helper.MainThreadHelper;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static fun.bm.chamomile.command.modules.executor.extra.sub.data.Query.dataGet;
import static fun.bm.chamomile.command.modules.executor.extra.sub.report.ReportQuery.message_head;
import static fun.bm.chamomile.data.processor.report.ImageProcessor.reportCharmProcess;
import static fun.bm.chamomile.util.MainEnv.LOGGER;
import static fun.bm.chamomile.util.helper.RconHelper.executeRconCommand;

/**
 * @author Suisuroru
 * Date: 2024/9/28 16:06
 * function: Check message if need rcon function
 */
public class ExecuteRcon extends Function {
    public static final List<Long> RconGroups = new ArrayList<>();

    public ExecuteRcon() {
        super("RCONCommandCheck");
    }

    public void onEnable() {
        if (!CoreConfig.official) {
            String enabledGroupStr = RconConfig.groups;
            if (enabledGroupStr == null || enabledGroupStr.isEmpty()) {
                LOGGER.warning("[RCONCommandCheck] RCON commands will be disabled due to empty or null RCONEnabledGroups");
                MainEnv.configManager.setConfigAndSave("bot.rcon.enabled", false);
                MainEnv.functionManager.reload();
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
                MainEnv.configManager.setConfigAndSave("bot.rcon.enabled", false);
                MainEnv.functionManager.reload();
                return;
            }
        }
        MainThreadHelper.botFuture.thenRun(() -> {
            MainEnv.eventChannel.subscribeAlways(GroupMessageEvent.class, event -> {
                if (!CoreConfig.official && !RconGroups.contains(event.getGroup().getId())) {
                    return;
                }

                Message message = event.getMessage();
                String content = message.contentToString();

                if (content.replace(" ", "").startsWith(RconConfig.prefix.replace(" ", ""))) {
                    String command = content.replace(RconConfig.prefix, "");
                    while (command.startsWith(" ")) command = command.substring(1);
                    boolean isOperator = false;
                    boolean isAuthenticated = false;
                    if (!CoreConfig.official) {
                        isOperator = event.getSender().getPermission().equals(MemberPermission.ADMINISTRATOR)
                                || event.getSender().getPermission().equals(MemberPermission.OWNER);
                    } else {
                        List<Data> dataList = dataGet.getPlayersByUserID(event.getGroup().getId());
                        if (!dataList.isEmpty()) {
                            for (Data data : dataList) {
                                isAuthenticated = true;
                                for (OfflinePlayer player : Bukkit.getServer().getOperators()) {
                                    if (player.getUniqueId().equals(data.playerData.playerUuid)) {
                                        isOperator = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!isAuthenticated) {
                        event.getGroup().sendMessage(new MessageChainBuilder()
                                .append(new PlainText("您还未绑定账户。"))
                                .build());
                        return;
                    }
                    if (RconConfig.enforceOperator) {
                        if (!isOperator) {
                            event.getGroup().sendMessage(new MessageChainBuilder()
                                    .append(new PlainText("您没有权限执行此操作。"))
                                    .build());
                            return;
                        }
                    }
                    String[] result = executeRconCommand(RconConfig.ip, RconConfig.port, RconConfig.password, command);
                    handleConsoleResult(result, event);
                }
            });
        });
    }

    private void handleConsoleResult(String[] result, GroupMessageEvent event) {
        try {
            if (result != null && !result[0].isEmpty()) {
                MessageChainBuilder message = new MessageChainBuilder();
                message.append(new PlainText(ServerConfig.serverName + "Console command result: \n"))
                        .append(result[0]);
                if (!result[1].isEmpty() && result[1].startsWith(message_head)) {
                    reportCharmProcess(result[1].substring(message_head.length()));
                    message.append(message_head)
                            .append(event.getGroup().uploadImage(ExternalResource.create(new File(MainEnv.BASE_DIR, "CharmProcess\\latest.png"))));
                } else {
                    message.append(result[1]);
                }
                event.getGroup().sendMessage(message.build());
            } else {
                event.getGroup().sendMessage(new MessageChainBuilder()
                        .append(new PlainText(ServerConfig.serverName + "No result from console command."))
                        .build());
            }
        } catch (Exception e) {
            event.getGroup().sendMessage(new MessageChainBuilder()
                    .append(new PlainText(ServerConfig.serverName + "\n[ERROR] " + e.getMessage()))
                    .build());
        }
    }

    public void setModuleName() {
        if (!CoreConfig.enabled || !RconConfig.enabled) {
            this.moduleName = null;
        }
    }
}
