package fun.suya.suisuroru.module.impl;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xd.suka.Main.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/10/22 23:08
 * function: Union Ban module for TUnite
 */
public class UnionBan {

    public ArrayList<BanPair<UUID, String, Date>> loadLocalBanList() {
        ArrayList<BanPair<UUID, String, Date>> banList = new ArrayList<>();

        // 获取游戏内的封禁名单（基于UUID和原因）
        ProfileBanList banListType = Bukkit.getServer().getBanList(ProfileBanList.Type.PROFILE);
        for (BanEntry<PlayerProfile> entry : banListType.getEntries()) {
            PlayerProfile profile = entry.getBanTarget();
            UUID uuid = profile.getUniqueId();
            String reason = entry.getReason();
            Date time = entry.getExpiration();
            banList.add(new BanPair<>(uuid, reason, time));
        }

        return banList;
    }

    public ArrayList<BanPair<UUID, String, Date>> loadRemoteBanList() {
        ArrayList<BanPair<UUID, String, Date>> banList = new ArrayList<>();
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

            // 解析 JSON 响应
            Pattern pattern = Pattern.compile("\"uuid\":\"([0-9a-fA-F-]+)\",\"reason\":\"([^\"]+)\",\"time\":\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(response.body());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

            while (matcher.find()) {
                UUID uuid = UUID.fromString(matcher.group(1));
                String reason = matcher.group(2);
                String timeStr = matcher.group(3);
                Date time = dateFormat.parse(timeStr);
                banList.add(new BanPair<>(uuid, reason, time));
            }

        } catch (IOException | InterruptedException | ParseException e) {
            LOGGER.info(String.valueOf(e));
        }

        return banList;
    }

    public static class BanPair<T1, T2, T3> {
        private T1 uuid;
        private T2 reason;
        private T3 time;

        public BanPair(T1 uuid, T2 reason, T3 time) {
            this.uuid = uuid;
            this.reason = reason;
            this.time = time;
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
    }
}
