package fun.bm.util.helper;

import com.google.gson.Gson;
import fun.bm.config.modules.ServerConfig;
import fun.bm.config.modules.WebhookConfig;

import java.util.Arrays;
import java.util.List;

import static fun.bm.util.HttpUtil.fetch;
import static fun.bm.util.MainEnv.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/9/26 21:21
 * function: Webhook for Email
 */
public class EmailSender {

    public String ensureValidUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        return url;
    }

    public void checkPlugin(String title) {
        try {
            String subject = "服务器" + title + "通知";
            String content = ServerConfig.serverName + "服务器已" + title + "完成";
            formatAndSendWebhook(subject, content, WebhookConfig.webHookEmails);
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
    }

    /**
     * 向指定的webhook地址发送邮件报告。
     *
     * @param data 包含邮件内容和主题的数据对象
     */

    private boolean processWebhookData(Data_Sub data) {
        try {
            // 确保 URL 格式正确
            String webhookUrl = ensureValidUrl(WebhookConfig.webhookUrl);

            // 构建 JSON 数据
            String jsonInputString = new Gson().toJson(data);

            if (fetch(webhookUrl, null, true, jsonInputString) == null) {
                return true;
            } else {
                LOGGER.info("Webhook sent successfully.");
                return false;
            }
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
            LOGGER.info("Error sending webhook: " + e.getMessage());
            return true;
        }
    }

    /**
     * 将邮件内容和主题封装为JSON格式并通过webhook发送。
     *
     * @param subject     邮件主题
     * @param content     邮件内容
     * @param originEmail 发送邮件的邮箱地址
     */
    public void formatAndSendWebhook(String subject, String content, String originEmail) {
        if (!WebhookConfig.enabled) return;
        List<String> emailList = Arrays.asList(originEmail.split(";"));
        Data_Full data = new Data_Full("来自" + ServerConfig.serverName + "的信息：\n" + content, subject, emailList);
        if (processWebhookData(data)) {
            Data_Sub data_new = new Data_Sub("来自" + ServerConfig.serverName + "的信息：\n" + content, subject);
            if (processWebhookData(data_new)) {
                LOGGER.warning("Failed to send webhook.");
            }
        }
    }

    /**
     * 报告数据类
     */
    private static class Data_Full extends Data_Sub {
        List<String> email;

        public Data_Full(String content, String subject, List<String> email) {
            super(content, subject);
            this.email = email;
        }
    }

    private static class Data_Sub {
        String content;
        String subject;

        public Data_Sub(String content, String subject) {
            this.content = content;
            this.subject = subject;
        }
    }
}
