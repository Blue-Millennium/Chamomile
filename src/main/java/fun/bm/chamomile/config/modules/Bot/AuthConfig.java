package fun.bm.chamomile.config.modules.Bot;

import fun.bm.chamomile.config.ConfigModule;
import fun.bm.chamomile.config.flags.ConfigInfo;

public class AuthConfig implements ConfigModule {

    @ConfigInfo(name = "enabled", comment = "启用QQ验证模块")
    public static boolean enabled = true;

    @ConfigInfo(name = "enforce-check", comment = "强制要求验证后才可登陆")
    public static boolean enforceCheck = false;

    @ConfigInfo(name = "connect-message", comment = "已验证登陆消息")
    public static String connectMessage = "[QQLogin] 您已经绑定了一个账户，如果需要删除或添加绑定，请使用/check [del|verify] [args...]";

    @ConfigInfo(name = "disconnect-message", comment = "未验证登陆消息/强制验证时被踢出服务器消息")
    public static String disconnectMessage = "[QQLogin] 请完成登录验证, 验证码: %CODE%";

    @ConfigInfo(name = "prefix", comment = "QQ验证前缀")
    public static String prefix = "/Check ";

    public String name() {
        return "auth";
    }

    public String[] category() {
        return new String[]{"bot"};
    }
}
