package fun.suya.suisuroru.message;

import com.google.gson.Gson;
import fun.suya.suisuroru.config.Config;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Suisuroru
 * Date: 2024/9/26 21:21
 * function: Webhook for Email
 */
public class Webhook4Email {

    /**
     * 向指定的webhook地址发送邮件报告。
     *
     * @param data 包含邮件内容和主题的数据对象
     */
    public void sendWebhookData(Data data) {
        try {
            // 确保 URL 格式正确
            String webhookUrl = ensureValidUrl(Config.webhookUrl);

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
                System.err.println("Unexpected response code: " + responseCode);
                System.err.println("Response body: " + response.body());
            } else {
                System.out.println("Webhook sent successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error sending webhook: " + e.getMessage());
        }
    }

    private String ensureValidUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        return url;
    }

    /**
     * 将邮件内容和主题封装为JSON格式并通过webhook发送。
     *
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void formatAndSendWebhook(String subject, String content) {
        Data data = new Data("来自" + Config.servername + "的信息：\n" + content, subject);
        sendWebhookData(data);
    }

    /**
     * 报告数据类
     */
    static class Data {
        String content;
        String subject;

        public Data(String content, String subject) {
            this.content = content;
            this.subject = subject;
        }

        public String getContent() {
            return content;
        }

        public String getSubject() {
            return subject;
        }
    }
}
