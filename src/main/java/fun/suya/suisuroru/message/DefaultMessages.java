package fun.suya.suisuroru.message;

import fun.suya.suisuroru.config.Config;

import static fun.xd.suka.Main.LOGGER;

public class DefaultMessages {
    public static void TurnOffPlugin() {
        try {
            String subject = "服务器关闭通知";
            String content = Config.ServerName + "服务器已关闭";
            WebhookForEmail webhookForEmail = new WebhookForEmail();
            webhookForEmail.formatAndSendWebhook(subject, content, Config.WebHookEmail);
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
    }

    public static void TurnOnPlugin() {
        try {
            String subject = "服务器启动通知";
            String content = Config.ServerName + "服务器已启动";
            WebhookForEmail webhookForEmail = new WebhookForEmail();
            webhookForEmail.formatAndSendWebhook(subject, content, Config.WebHookEmail);
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
    }
}
