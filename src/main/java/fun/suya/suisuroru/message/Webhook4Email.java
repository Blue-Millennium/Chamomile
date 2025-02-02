package fun.suya.suisuroru.message;

import com.google.gson.Gson;
import fun.suya.suisuroru.config.Config;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static fun.xd.suka.Main.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/9/26 21:21
 * function: Webhook for Email
 */
public class Webhook4Email {

    public static String ensureValidUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        return url;
    }

    /**
     * 向指定的webhook地址发送邮件报告。
     *
     * @param data 包含邮件内容和主题的数据对象
     */
    public void sendWebhookData(Data data) {
        try {
            // 确保 URL 格式正确
            String webhookUrl = ensureValidUrl(Config.WebhookUrl);

            // 创建 HttpClient 实例
            HttpClient httpClient = HttpClient.newHttpClient();

            // 构建 JSON 数据
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
                LOGGER.info("Unexpected response code: " + responseCode);
                LOGGER.info("Response body: " + response.body());
            } else {
                LOGGER.info("Webhook sent successfully.");
            }

        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
            LOGGER.info("Error sending webhook: " + e.getMessage());
        }
    }

    /**
     * 将邮件内容和主题封装为JSON格式并通过webhook发送。
     *
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param originEmail    发送邮件的邮箱地址
     */
    public void formatAndSendWebhook(String subject, String content, String originEmail) {
        List<String> emailList = Arrays.asList(originEmail.split(";"));
        String emailString = emailList.stream()
                .map(email -> "\"" + email + "\"")
                .collect(Collectors.joining(", ", "[", "]"));

        Data data = new Data("来自" + Config.ServerName + "的信息：\n" + content, subject, emailString);
        LOGGER.info("Webhook data: " + new Gson().toJson(data));
        sendWebhookData(data);
    }

    /**
     * 报告数据类
     */
    static class Data {
        String content;
        String subject;
        String email;

        public Data(String content, String subject, String email) {
            this.content = content;
            this.subject = subject;
            this.email = email;
        }
    }
}
