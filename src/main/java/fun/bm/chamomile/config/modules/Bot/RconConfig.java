package fun.bm.chamomile.config.modules.Bot;

import fun.bm.chamomile.config.ConfigModule;
import fun.bm.chamomile.config.flags.ConfigInfo;

public class RconConfig implements ConfigModule {

    @ConfigInfo(name = "enabled", comment = "启用Rcon模块")
    public static boolean enabled = false;

    @ConfigInfo(name = "prefix", comment = "Rcon执行命令前缀")
    public static String prefix = "/Rcon ";

    @ConfigInfo(name = "enforce-operator", comment = "强制Rcon需要操作员权限，非官方模式下默认将综合考虑群权限和游戏内管理员，官方机器人模式以游戏内管理员为准")
    public static boolean enforceOperator = true;

    @ConfigInfo(name = "allow-group-permissions", comment = "允许群管理员使用Rcon")
    public static boolean allowGroupPermissions = true;

    @ConfigInfo(name = "allow-player-permissions", comment = "允许游戏内管理员使用Rcon")
    public static boolean allowPlayerPermissions = true;

    @ConfigInfo(name = "ip", comment = "Rcon监听地址")
    public static String ip = "0.0.0.0";

    @ConfigInfo(name = "port", comment = "Rcon监听端口")
    public static int port = 25575;

    @ConfigInfo(name = "password", comment = "Rcon密码")
    public static String password = "password";

    @ConfigInfo(name = "enabled-groups", comment = "允许Rcon的QQ群，多个时以分号分割（仅非官方机器人模式有效）")
    public static String groups = "123456;234567";

    public String name() {
        return "rcon";
    }

    public String[] category() {
        return new String[]{"bot"};
    }
}
