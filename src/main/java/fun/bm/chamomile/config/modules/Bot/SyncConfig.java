package fun.bm.chamomile.config.modules.Bot;

import fun.bm.chamomile.config.flags.ConfigInfo;

public class SyncConfig extends AbstractBotConfig {

    @ConfigInfo(name = "enabled", comment = "启用QQ与服务器同步聊天")
    public static boolean enabled = false;

    @ConfigInfo(name = "qq-to-server-only", comment = "仅同步QQ的消息到服务器")
    public static boolean qqToServerOnly = false;

    @ConfigInfo(name = "join-message", comment = "加入服务器消息")
    public static String joinMessage = "%NAME% joined the server";

    @ConfigInfo(name = "leave-message", comment = "离开服务器消息")
    public static String leaveMessage = "%NAME% left the server";

    @ConfigInfo(name = "qq-message", comment = "QQ消息格式")
    public static String qqMessage = "[QQ] %NAME%: %MESSAGE%";

    @ConfigInfo(name = "server-message", comment = "服务器消息格式")
    public static String serverMessage = "%NAME%: %MESSAGE%";

    @ConfigInfo(name = "groups", comment = "QQ群组ID，多个时以分号分割（仅非官方机器人）")
    public static String groups = "123456;234567";

    @ConfigInfo(name = "prefix", comment = "同步消息前缀（仅官方机器人）")
    public static String prefix = "/Send";

    public String name() {
        return "sync-chat";
    }
}
