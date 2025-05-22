package fun.bm.config.modules;

import fun.bm.config.ConfigInfo;
import fun.bm.config.ConfigModule;

public class WebhookConfig implements ConfigModule {

    @ConfigInfo(name = "enabled", comment = "启用WebHook邮箱推送服务")
    public static boolean enabled = false;

    @ConfigInfo(name = "url", comment = "Webhook邮箱服务推送地址")
    public static String webhookUrl = "http://localhost:6888/webhook";

    @ConfigInfo(name = "emails", comment = "信息推送邮箱，多个时以分号分割")
    public static String webHookEmails = "email1@example.com;email2@example.com";

    public String name() {
        return "webhook";
    }
}
