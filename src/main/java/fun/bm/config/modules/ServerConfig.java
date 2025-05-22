package fun.bm.config.modules;

import fun.bm.config.ConfigInfo;
import fun.bm.config.ConfigModule;

public class ServerConfig implements ConfigModule {

    @ConfigInfo(name = "name", comment = "服务器名称")
    public static String serverName = "ServerName";

    @ConfigInfo(name = "disable-damage", comment = "玩家处于创造和旁观模式时，免疫一切伤害（除/cm kill）")
    public static boolean damageDisabled = true;

    @ConfigInfo(name = "vanilla-commands-rewritten", comment = "是否启用重写的原版命令（启用UnionBan的推送服务必须启用此配置文件）")
    public static boolean vanillaCommandsRewritten = true;

    public String name() {
        return "server";
    }
}
