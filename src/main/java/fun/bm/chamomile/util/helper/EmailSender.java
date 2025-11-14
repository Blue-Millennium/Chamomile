package fun.bm.chamomile.util.helper;

import fun.bm.chamomile.config.modules.ServerConfig;
import fun.bm.chamomile.config.modules.WebhookConfig;
import fun.bm.chamomile.util.GsonUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static fun.bm.chamomile.util.Environment.LOGGER;
import static fun.bm.chamomile.util.HttpUtil.fetch;

/**
 * @author Suisuroru
 * Date: 2024/9/26 21:21
 * function: Webhook for Email
 */
public class EmailSender {
    /**
     * 向指定的webhook地址发送邮件报告。
     *
     * @param data 包含邮件内容和主题的数据对象
     */

    private static boolean processWebhookData(Data_Sub data) {
        try {
            String jsonInputString = GsonUtil.createGson().toJson(data);

            if (fetch(WebhookConfig.webhookUrl, null, true, jsonInputString) == null) {
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
    public static void formatAndSendWebhook(String subject, String content, @Nullable String originEmail) {
        if (!WebhookConfig.enabled) return;
        boolean runWithError;
        if (originEmail == null) {
            runWithError = true;
        } else {
            List<String> emailList = Arrays.asList(originEmail.split(";"));
            Data_Full data = new Data_Full("来自" + ServerConfig.serverName + "的信息：\n" + content, subject, emailList);
            runWithError = processWebhookData(data);
        }
        if (runWithError) {
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
