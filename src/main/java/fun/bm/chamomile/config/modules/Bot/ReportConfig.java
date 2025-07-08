package fun.bm.chamomile.config.modules.Bot;

import fun.bm.chamomile.config.ConfigModule;
import fun.bm.chamomile.config.flags.ConfigInfo;

public class ReportConfig implements ConfigModule {

    @ConfigInfo(name = "enabled", comment = "启用举报消息推送至QQ（仅非官方机器人模式）")
    public static boolean enabled = false;

    @ConfigInfo(name = "groups", comment = "QQ群组ID，多个时以分号分割（仅非官方机器人）")
    public static String groups = "123456;234567";

    @ConfigInfo(name = "message", comment = "QQ消息格式")
    public static String message = "%NAME% was logging in \nIP: %IP% %IPINFO% \nLoginResult: %RESULT%";

    public String name() {
        return "report";
    }

    public String[] category() {
        return new String[]{"bot"};
    }
}
