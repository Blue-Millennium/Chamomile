package fun.bm.data.UnionBan.OnlineProcessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fun.bm.config.Config;
import fun.bm.data.UnionBan.LocalProcessor.UnionBanDataGet;
import fun.bm.data.UnionBan.UnionBanData;
import fun.bm.util.helper.EmailSender;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static fun.bm.util.MainEnv.LOGGER;
import static fun.bm.util.helper.EncryptHelper.encrypt;

public class OnlinePush {
    public static Boolean reportRemoteBanList(UnionBanData data) {
        // 创建 JSON 数据
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode banDataNode = objectMapper.createObjectNode();
        banDataNode.put("playerUuid", String.valueOf(data.playerUuid));
        banDataNode.put("reason", data.reason);
        banDataNode.put("time", data.time);
        banDataNode.put("sourceServer", data.sourceServer);
        String base64EncodedJson = null;
        String json_fin = null;
        try {
            String json = objectMapper.writeValueAsString(banDataNode);
            base64EncodedJson = encrypt(json, Config.UnionBanReportKey);
            ObjectNode dataNode_new = objectMapper.createObjectNode();
            dataNode_new.put("data", base64EncodedJson);
            json_fin = objectMapper.writeValueAsString(dataNode_new);
        } catch (JsonProcessingException e) {
            LOGGER.warning("Failed to convert UnionBan data to JSON: " + e.getMessage());
        }

        if (base64EncodedJson == null) {
            return false;
        }

        // 确保 URL 格式正确
        String reportUrl = EmailSender.ensureValidUrl(Config.UnionBanReportUrl);

        try {
            // 创建 HttpClient 实例
            HttpClient httpClient = HttpClient.newHttpClient();

            // 创建 HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(reportUrl))
                    .header("Content-Type", "application/json; utf-8")
                    .POST(HttpRequest.BodyPublishers.ofString(json_fin))
                    .build();

            // 发送请求并获取响应
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 处理响应
            if (response.statusCode() == 200) {
                LOGGER.info("封禁数据上报成功");
                data.reportTag = true;
                UnionBanDataGet dp = new UnionBanDataGet();
                dp.setPlayerData(data.playerUuid, data);
                return true;
            } else {
                LOGGER.info("封禁数据上报失败，状态码: " + response.statusCode());
                LOGGER.info("回传信息: " + response);
            }

        } catch (IOException | InterruptedException e) {
            LOGGER.info(String.valueOf(e));
        }
        return false;
    }
}
