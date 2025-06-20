package fun.bm.util;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static fun.bm.util.MainEnv.LOGGER;

public class HttpUtil {
    public static byte[] fetch(String url, @Nullable String[] header, boolean isPost, @Nullable String post) {
        if (header == null || header.length == 0) {
            header = new String[]{"Content-Type", "application/json; utf-8", "Accept", "application/json"};
        } else if (header.length % 2 != 0) {
            LOGGER.warning("header length must be even");
            return null;
        }
        try {
            // 创建 HttpClient 实例
            HttpClient httpClient = HttpClient.newHttpClient();

            // 创建 HttpRequest
            HttpRequest.Builder request = HttpRequest.newBuilder()
                    .uri(URI.create(url));
            for (int i = 0; i < header.length; i += 2) {
                request.header(header[i], header[i + 1]);
            }

            if (isPost && post != null) {
                request.POST(HttpRequest.BodyPublishers.ofString(post));
            }

            // 发送请求并获取响应
            HttpResponse<String> response = httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString());
            // 检查响应状态码
            int responseCode = response.statusCode();
            if (responseCode != 200) {
                LOGGER.info("Unexpected response code: " + responseCode);
                LOGGER.info("Response body: " + response.body());
                return null;
            } else {
                return response.body().getBytes();
            }
        } catch (Exception e) {
            LOGGER.warning("Error fetch data: " + e.getMessage());
            return null;
        }
    }
}
