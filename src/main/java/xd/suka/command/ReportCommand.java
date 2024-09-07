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
import com.google.gson.Gson;
import xd.suka.Main;
import xd.suka.config.Config;
import xd.suka.module.impl.Reporter;
import xd.suka.util.TimeUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
                    if (Bukkit.getServer().getPlayer(args[0]) == null) {
                        sender.sendMessage("§c玩家不存在");
                    } else if (Objects.requireNonNull(Bukkit.getServer().getPlayer(args[0])).getUniqueId().equals(((Player) sender).getUniqueId())) {
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
                            // 确保 URL 格式正确
                            String webhookUrl = ensureValidUrl(Config.webhookUrl);

                            // 创建 HttpClient 实例
                            HttpClient httpClient = HttpClient.newHttpClient();

                            // 构建 JSON 数据
                            ReportData data = new ReportData(content, "玩家举报-" + number);
                            String jsonInputString = new Gson().toJson(data);

                            // 创建 HttpRequest
                            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create(webhookUrl))
                                    .header("Content-Type", "application/json; utf-8")
                                    .header("Accept", "application/json")
                                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                                    .build();

                            // 发送请求并获取响应
                            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                            // 检查响应状态码
                            int responseCode = response.statusCode();
                            if (responseCode != 200) {
                                System.err.println("Unexpected response code: " + responseCode);
                                System.err.println("Response body: " + response.body());
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

    /**
     * 确保 URL 格式正确
     *
     * @param url 原始 URL
     * @return 格式正确的 URL
     */
    private String ensureValidUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        return url;
    }

    /**
     * 报告数据类
     */
    static class ReportData {
        String content;
        String subject;

        public ReportData(String content, String subject) {
            this.content = content;
            this.subject = subject;
        }
    }
}
