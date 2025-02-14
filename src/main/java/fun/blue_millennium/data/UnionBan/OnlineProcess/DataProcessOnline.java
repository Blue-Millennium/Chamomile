package fun.blue_millennium.data.UnionBan.OnlineProcess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fun.blue_millennium.config.Config;
import fun.blue_millennium.data.UnionBan.LocalProcess.UnionBanData;
import fun.blue_millennium.data.UnionBan.LocalProcess.UnionBanDataGet;
import fun.blue_millennium.message.WebhookForEmail;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static fun.blue_millennium.Main.LOGGER;

public class DataProcessOnline {
    public static ArrayList<UnionBanData> loadRemoteBanList() {
        ArrayList<UnionBanData> banList = new ArrayList<>();
        // 确保 URL 格式正确
        String checkUrl = WebhookForEmail.ensureValidUrl(Config.UnionBanCheckUrl);

        try {
            // 创建 HttpClient 实例
            HttpClient httpClient = HttpClient.newHttpClient();

            // 创建 HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(checkUrl))
                    .header("Content-Type", "application/json; utf-8")
                    .header("Accept", "application/json")
                    .build();

            // 发送请求并获取响应
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            List<UnionBanData> remoteBans = objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, UnionBanData.class));
            banList.addAll(remoteBans);

        } catch (IOException | InterruptedException e) {
            LOGGER.info(String.valueOf(e));
        }

        return banList;
    }

    public static void reportRemoteBanList(UnionBanData data) {
        // 创建 JSON 数据
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode dataNode = objectMapper.createObjectNode();
        dataNode.put("secret", Config.UnionBanReportKey);
        ObjectNode banDataNode = dataNode.putObject("data");
        banDataNode.put("playerUuid", String.valueOf(data.playerUuid));
        banDataNode.put("reason", data.reason);
        banDataNode.put("time", data.time);
        banDataNode.put("sourceServer", data.sourceServer);
        String json = null;

        try {
            json = objectMapper.writeValueAsString(dataNode);
        } catch (JsonProcessingException e) {
            LOGGER.warning("Failed to convert UnionBan data to JSON: " + e.getMessage());
        }

        if (json == null) {
            return;
        }

        // 确保 URL 格式正确
        String reportUrl = WebhookForEmail.ensureValidUrl(Config.UnionBanReportUrl);

        try {
            // 创建 HttpClient 实例
            HttpClient httpClient = HttpClient.newHttpClient();

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
                data.reportTag = true;
                UnionBanDataGet dp = new UnionBanDataGet();
                dp.setPlayerData(data.playerUuid, data);
            } else {
                LOGGER.info("封禁数据上报失败，状态码: " + response.statusCode());
                LOGGER.info("回传信息: " + response);
            }

        } catch (IOException | InterruptedException e) {
            LOGGER.info(String.valueOf(e));
        }
    }
}
