package fun.bm.util;

import com.google.gson.Gson;
import fun.bm.util.map.IpLocationMap;
import fun.bm.util.map.IpinfoMap;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static fun.bm.util.MainEnv.LOGGER;

public class IpInfoUtil {
    public static IpinfoMap getIpinfo(String ip) {
        try {
            // 创建 HttpClient 实例
            HttpClient httpClient = HttpClient.newHttpClient();

            // 创建 HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://ipinfo.io/widget/demo/" + ip))
                    .header("Content-Type", "application/json; utf-8")
                    .header("Accept", "application/json")
                    .build();

            // 发送请求并获取响应
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            // 检查响应状态码
            int responseCode = response.statusCode();
            if (responseCode != 200) {
                LOGGER.info("Unexpected response code: " + responseCode);
                LOGGER.info("Response body: " + response.body());
                return null;
            } else {
                return new Gson().fromJson(response.body(), IpinfoMap.class);
            }
        } catch (Exception e) {
            LOGGER.warning("Error getting IP info: " + e.getMessage());
            return null;
        }
    }

    public static IpLocationMap getIpinfoCN(String ip) {
        try {
            // 创建 HttpClient 实例
            HttpClient httpClient = HttpClient.newHttpClient();

            // 创建 HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://webapi-pc.meitu.com/common/ip_location?ip=" + ip))
                    .header("Content-Type", "application/json; utf-8")
                    .header("Accept", "application/json")
                    .build();

            // 发送请求并获取响应
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            // 检查响应状态码
            int responseCode = response.statusCode();
            if (responseCode != 200) {
                LOGGER.info("Unexpected response code: " + responseCode);
                LOGGER.info("Response body: " + response.body());
                return null;
            } else {
                return new Gson().fromJson(response.body(), IpLocationMap.class);
            }
        } catch (Exception e) {
            LOGGER.warning("Error getting IP info: " + e.getMessage());
            return null;
        }
    }
}
