package xd.suka.command;

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
                sender.sendMessage("§c/bpReport <玩家名> <原因>");
            } else {
                if (reportGroup != null) {
                    if (Bukkit.getServer().getPlayer(args[0]) == null)  {
                        sender.sendMessage("§c玩家不存在");
                    } else if(Objects.requireNonNull(Bukkit.getServer().getPlayer(args[0])).getUniqueId().equals(((Player) sender).getUniqueId())) {
                        sender.sendMessage("§c你不能对自己使用");
                    } else {
                            MessageChainBuilder builder = new MessageChainBuilder();
                            String number = String.valueOf(System.currentTimeMillis());
                            if (reportGroup.getBotPermission() == MemberPermission.ADMINISTRATOR || reportGroup.getBotPermission() == MemberPermission.OWNER) {
                                builder.append(AtAll.INSTANCE).append(" ");
                            }
                            builder.append(TimeUtil.getNowTime()).append('\n').append("玩家 ").append(args[0]).append(" 被 ").append(sender.getName()).append(" 报告，原因：").append(args.length > 1 ? args[1] : "无").append('\n').append("编号: ").append(number);
                            reportGroup.sendMessage(builder.build());
                            sender.sendMessage("§a已发送报告  编号: " + number);
                    }
                } else {
                    sender.sendMessage("§c内部错误");
                }
            }
        }

        return true;
    }
}
