package xd.suka.command;

import fun.suya.suisuroru.config.Config;
import fun.suya.suisuroru.data.ReportDataManager;
import fun.suya.suisuroru.message.Webhook4Email;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.message.data.AtAll;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xd.suka.Main;
import xd.suka.module.impl.Reporter;
import xd.suka.util.TimeUtil;

import java.util.Objects;

public class ReportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Group reportGroup = ((Reporter) Main.INSTANCE.moduleManager.getModuleByName("Reporter")).reportGroup;

            if (args.length == 0) {
                if (command.getName().equals("report")) {
                    sender.sendMessage("§c/report <玩家名> <原因>");
                } else if (command.getName().equals("baseplugin")) {
                    sender.sendMessage("§c/baseplugin report <玩家名> <原因>");
                } else if (command.getName().equals("bp")) {
                    sender.sendMessage("§c/bp report <玩家名> <原因>");
                }
            } else {
                if (reportGroup != null) {
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
                            Webhook4Email webhook4Email = new Webhook4Email();
                            webhook4Email.formatAndSendWebhook(subject, content);
                        } catch (Exception e) {
                            sender.sendMessage("§c邮件发送失败");
                            e.printStackTrace();
                        }
                        MessageChainBuilder builder_qq = new MessageChainBuilder();

                        if (Config.QQRobotEnabled) {
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