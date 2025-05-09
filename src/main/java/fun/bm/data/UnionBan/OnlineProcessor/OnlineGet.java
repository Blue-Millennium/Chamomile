package fun.bm.data.UnionBan.OnlineProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.bm.config.Config;
import fun.bm.data.UnionBan.UnionBanData;
import fun.bm.util.MainEnv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static fun.bm.util.MainEnv.LOGGER;

public class OnlineGet {
    public static ArrayList<UnionBanData> loadRemoteBanList() {
        ArrayList<UnionBanData> banList = new ArrayList<>();
        // 确保 URL 格式正确
        String checkUrl = MainEnv.emailSender.ensureValidUrl(Config.UnionBanCheckUrl);

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
}
