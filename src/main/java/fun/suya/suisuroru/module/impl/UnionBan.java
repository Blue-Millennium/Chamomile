package fun.suya.suisuroru.module.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fun.suya.suisuroru.config.Config;
import fun.suya.suisuroru.message.Webhook4Email;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.profile.PlayerProfile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static fun.suya.suisuroru.data.UnionBan.LocalCache.save;
import static xd.suka.Main.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/10/22 23:08
 * function: Union Ban module for TUnite
 */
public class UnionBan {

    public static ArrayList<BanPair<UUID, String, Date, String>> loadLocalBanList() {
        ArrayList<BanPair<UUID, String, Date, String>> banList = new ArrayList<>();

        // 获取游戏内的封禁名单（基于UUID、原因和封禁来源服务器）
        ProfileBanList banListType = Bukkit.getServer().getBanList(ProfileBanList.Type.PROFILE);
        for (BanEntry<PlayerProfile> entry : banListType.getEntries()) {
            PlayerProfile profile = entry.getBanTarget();
            UUID uuid = profile.getUniqueId();
            String reason = entry.getReason();
            Date time = entry.getExpiration();
            String sourceServer = "Local";
            banList.add(new BanPair<>(uuid, reason, time, sourceServer));
        }

        return banList;
    }

    public static ArrayList<BanPair<UUID, String, Date, String>> loadRemoteBanList() {
        ArrayList<BanPair<UUID, String, Date, String>> banList = new ArrayList<>();
        // 确保 URL 格式正确
        String webhookUrl = Webhook4Email.ensureValidUrl(Config.webhookUrl);

        try {
            // 创建 HttpClient 实例
            HttpClient httpClient = HttpClient.newHttpClient();

            // 创建 HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json; utf-8")
                    .header("Accept", "application/json")
                    .build();

            // 发送请求并获取响应
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 使用 Jackson 解析 JSON 响应
            ObjectMapper objectMapper = new ObjectMapper();
            List<BanPair<UUID, String, Date, String>> remoteBans = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, BanPair.class));

            banList.addAll(remoteBans);

        } catch (IOException | InterruptedException e) {
            LOGGER.info(String.valueOf(e));
        }

        return banList;
    }

    public static void reportBanData(BanPair<UUID, String, Date, String> banPair) {
        if (!Config.UnionBanCheckOnly) {
            // 确保 URL 格式正确
            String reportUrl = Webhook4Email.ensureValidUrl(Config.UnionBanReportUrl);

            try {
                // 创建 HttpClient 实例
                HttpClient httpClient = HttpClient.newHttpClient();

                // 使用 Jackson 生成 JSON 数据
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode dataNode = objectMapper.createObjectNode();
                dataNode.put("secret", Config.UnionBanReportKey);
                ObjectNode banDataNode = dataNode.putObject("data");
                banDataNode.put("uuid", banPair.getUUID().toString());
                banDataNode.put("reason", banPair.getReason());
                banDataNode.put("time", banPair.getTime().toString());
                banDataNode.put("sourceServer", banPair.getSourceServer());

                String json = objectMapper.writeValueAsString(dataNode);

                // 创建 HttpRequest
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(reportUrl))
                        .header("Content-Type", "application/json; utf-8")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                // 发送请求并获取响应
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                // 处理响应
                if (response.statusCode() == 200) {
                    LOGGER.info("封禁数据上报成功");
                } else {
                    LOGGER.info("封禁数据上报失败，状态码: " + response.statusCode());
                    LOGGER.info("回传信息: " + response);
                    save(banPair);
                }

            } catch (IOException | InterruptedException e) {
                LOGGER.info(String.valueOf(e));
            }
        }
    }

    public static void checkBanData() {
        if (Config.UnionBanEnabled) {
            // 确保 URL 格式正确
            String checkUrl = Webhook4Email.ensureValidUrl(Config.UnionBanCheckUrl);

            try {
                // 创建 HttpClient 实例
                HttpClient httpClient = HttpClient.newHttpClient();
                // 创建 HttpRequest
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(checkUrl))
                        .header("Content-Type", "application/json; utf-8")
                        .build();

                // 发送请求并获取响应
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                // 使用 Jackson 解析 JSON 响应
                ObjectMapper objectMapper = new ObjectMapper();
                List<BanPair<UUID, String, Date, String>> remoteBans = objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, BanPair.class));

                // 获取本地的封禁列表
                ProfileBanList banListType = Bukkit.getServer().getBanList(ProfileBanList.Type.PROFILE);

                // 遍历远程封禁数据并应用到本地
                for (BanPair<UUID, String, Date, String> banPair : remoteBans) {
                    UUID uuid = banPair.getUUID();
                    String reason = banPair.getReason();
                    Date expiration = banPair.getTime();
                    String sourceServer = banPair.getSourceServer();

                    // 检查本地是否已经存在相同的封禁记录
                    if (!banListType.isBanned(String.valueOf(uuid))) {
                        // 添加新的封禁记录到本地
                        boolean result;
                        if (Objects.equals(reason, "Pardon")) {
                            result = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:pardon " + uuid);
                        } else {
                            result = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:ban " + uuid + " " + reason);
                        }
                        if (result) {
                            LOGGER.info("从远程服务器同步封禁数据: UUID=" + uuid + ", 原因=" + reason + ", 过期时间=" + expiration + ", 来源服务器=" + sourceServer);
                        }
                    } else {
                        LOGGER.info("本地已存在相同的封禁记录: UUID=" + uuid);
                    }
                }

            } catch (Exception e) {
                LOGGER.info(e.getMessage());
            }
        }
    }


    public static class BanPair<T1, T2, T3, T4> {
        private T1 uuid;
        private T2 reason;
        private T3 time;
        private T4 sourceServer;

        public BanPair(T1 uuid, T2 reason, T3 time, T4 sourceServer) {
            this.uuid = uuid;
            this.reason = reason;
            this.time = time;
            this.sourceServer = sourceServer;
        }

        public T1 getUUID() {
            return uuid;
        }

        public T2 getReason() {
            return reason;
        }

        public T3 getTime() {
            return time;
        }

        public T4 getSourceServer() {
            return sourceServer;
        }

        public void changeSource(T4 sourceServer) {
            this.sourceServer = sourceServer;
        }
    }
}
