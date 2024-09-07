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
import xd.suka.config.Config;
import xd.suka.module.impl.Reporter;
import xd.suka.util.TimeUtil;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

                            String content = builder.build().contentToString();

                            try {
                                // 创建URL对象
                                URL url = new URL(Config.webhookUrl);
                                // 打开连接
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                // 设置请求方法为POST
                                connection.setRequestMethod("POST");
                                // 设置请求头
                                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                                connection.setRequestProperty("Accept", "application/json");
                                // 允许输出流
                                connection.setDoOutput(true);

                                // 构建JSON数据
                                String jsonInputString = "{\"content\": \"" + content + "\", \"subject\": \"玩家举报-" + number + "\"}";

                                connection.setRequestProperty("Content-Length", Integer.toString(jsonInputString.length()));
                                connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));

                                // 获取响应码
                                int responseCode = connection.getResponseCode();
                                if (responseCode != 200) {
                                    System.out.println("Response Code: " + responseCode);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

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
