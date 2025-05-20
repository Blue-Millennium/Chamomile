package fun.bm.command.main.executor.extra.sub.report;

import fun.bm.command.Command;
import fun.bm.config.old.Config;
import fun.bm.data.manager.report.ReportDataManager;
import fun.bm.util.MainEnv;
import fun.bm.util.TimeUtil;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.message.data.AtAll;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static fun.bm.module.impl.QQReporter.ReportGroups;
import static fun.bm.util.MainEnv.LOGGER;

public class Report extends Command.ExecutorE {
    public static ReportDataManager reportDataManager = new ReportDataManager();

    public Report() {
        super("report");
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                switch (command.getName()) {
                    case "report" -> sender.sendMessage("§c/report <玩家名> <原因>");
                    case "chamomile" -> sender.sendMessage("§c/chamomile report <玩家名> <原因>");
                    case "cm" -> sender.sendMessage("§c/cm report <玩家名> <原因>");
                }
            } else {
                if (Bukkit.getServer().getPlayer(args[0]) == null) {
                    sender.sendMessage("§c玩家不存在");
                } else if (Objects.requireNonNull(Bukkit.getServer().getPlayer(args[0])).getUniqueId().equals(((Player) sender).getUniqueId())) {
                    sender.sendMessage("§c你不能对自己使用");
                } else {
                    // Data Save
                    reportDataManager.ProcessData(sender, args);
                    sender.sendMessage("§a举报已被记录，正在等待上报");
                    // Message Send
                    MessageChainBuilder builder = new MessageChainBuilder();
                    String number = String.valueOf(System.currentTimeMillis());
                    StringBuilder reasonBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        reasonBuilder.append(args[i]).append(" ");
                    }
                    String reason = reasonBuilder.toString().trim();
                    builder.append(TimeUtil.getNowTime()).append('\n')
                            .append("玩家 ").append(args[0]).append(" 被 ")
                            .append(sender.getName()).append(" 报告，原因：")
                            .append(reason.isEmpty() ? "无" : reason)
                            .append('\n')
                            .append("编号: ").append(number);
                    String content = builder.build().contentToString();
                    try {
                        String subject = "玩家举报-" + number;
                        MainEnv.emailSender.formatAndSendWebhook(subject, content, Config.WebHookEmail);
                        sender.sendMessage("§a举报已被上报至指定邮箱");
                    } catch (Exception e) {
                        sender.sendMessage("§c邮件发送失败");
                        LOGGER.warning(e.getMessage());
                    }

                    if (Config.QQRobotEnabled && !Config.BotModeOfficial && !ReportGroups.isEmpty()) {
                        for (long groupId : ReportGroups) {
                            MessageChainBuilder builder_qq = new MessageChainBuilder();
                            Group reportGroup = MainEnv.BOT.getGroup(groupId);
                            if (reportGroup.getBotPermission() == MemberPermission.ADMINISTRATOR || reportGroup.getBotPermission() == MemberPermission.OWNER) {
                                builder_qq.append(" ").append(AtAll.INSTANCE).append("\n");
                            }

                            builder_qq.append(TimeUtil.getNowTime()).append('\n')
                                    .append("玩家 ").append(args[0]).append(" 被 ")
                                    .append(sender.getName()).append(" 报告，原因：")
                                    .append(reason.isEmpty() ? "无" : reason)
                                    .append('\n')
                                    .append("编号: ").append(number);

                            reportGroup.sendMessage(builder_qq.build());
                            sender.sendMessage("§a已发送报告  编号: " + number);
                        }
                    }
                }
            }
        } else {
            sender.sendMessage("§c只有玩家才能使用");
        }
        return true;
    }
}