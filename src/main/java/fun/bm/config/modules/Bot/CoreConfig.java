package fun.bm.config.modules.Bot;

import fun.bm.config.rewritten.ConfigInfo;
import fun.bm.config.rewritten.ConfigModule;

public class CoreConfig implements ConfigModule {

    @ConfigInfo(name = "enabled", comment = "启用Bot模块")
    public static boolean enabled = false;

    @ConfigInfo(name = "ws-url", comment = "Bot WebSocket地址")
    public static String wsUrl = "ws://0.0.0.0:3001";

    @ConfigInfo(name = "ws-token", comment = "Bot WebSocket鉴权Token")
    public static String wsToken = "your-bot-key";

    @ConfigInfo(name = "login-check-period", comment = "Bot掉线检查周期（-1为禁用）")
    public static int loginCheckPeriod = 1200;

    @ConfigInfo(name = "official", comment = "是否使用官方Bot模式")
    public static boolean official = true;

    public String name() {
        return "core";
    }

    public String[] category() {
        return new String[]{"bot"};
    }
}
