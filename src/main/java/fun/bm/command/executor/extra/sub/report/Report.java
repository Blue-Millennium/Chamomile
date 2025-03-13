package fun.bm.command.executor.extra.sub.report;

import fun.bm.command.Command;
import fun.bm.config.Config;
import fun.bm.data.Report.ReportDataManager;
import fun.bm.util.TimeUtil;
import fun.bm.util.helper.EmailSender;
import fun.bm.util.helper.MainEnv;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.message.data.AtAll;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static fun.bm.module.impl.Reporter.ReportGroups;
import static fun.bm.util.helper.MainEnv.LOGGER;

public class Report extends Command.ExecutorE {
    public Report() {
        super("report");
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                if (command.getName().equals("report")) {
                    sender.sendMessage("§c/report <玩家名> <原因>");
                } else if (command.getName().equals("chamomile")) {
                    sender.sendMessage("§c/chamomile report <玩家名> <原因>");
                } else if (command.getName().equals("cm")) {
                    sender.sendMessage("§c/cm report <玩家名> <原因>");
                }
            } else {
                if (!ReportGroups.isEmpty()) {
                    if (Bukkit.getServer().getPlayer(args[0]) == null) {
                        sender.sendMessage("§c玩家不存在");
                    } else if (Objects.requireNonNull(Bukkit.getServer().getPlayer(args[0])).getUniqueId().equals(((Player) sender).getUniqueId())) {
                        sender.sendMessage("§c你不能对自己使用");
                    } else {
                        // Data Save
                        ReportDataManager manager = new ReportDataManager();
                        manager.ProcessData(sender, args);
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
                            EmailSender emailSender = new EmailSender();
                            emailSender.formatAndSendWebhook(subject, content, Config.WebHookEmail);
                        } catch (Exception e) {
                            sender.sendMessage("§c邮件发送失败");
                            LOGGER.warning(e.getMessage());
                        }

                        if (Config.QQRobotEnabled && !Config.BotModeOfficial) {
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
                } else {
                    sender.sendMessage("§c内部错误");
                }
            }
        } else {
            sender.sendMessage("§c只有玩家才能使用");
        }
        return true;
    }
}