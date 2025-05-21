package fun.bm.config.modules.Bot;

import fun.bm.config.rewritten.ConfigInfo;
import fun.bm.config.rewritten.ConfigModule;

public class RconConfig implements ConfigModule {

    @ConfigInfo(name = "enable", comment = "启用Rcon模块")
    public static boolean enabled = false;

    @ConfigInfo(name = "prefix", comment = "Rcon执行命令前缀")
    public static String prefix = "/Rcon ";

    @ConfigInfo(name = "enforce-operator", comment = "强制Rcon需要操作员权限，官方模式下默认以群权限为准，官方机器人模式以游戏内管理员为准")
    public static boolean enforceOperator = true;

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
