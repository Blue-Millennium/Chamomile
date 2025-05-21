package fun.bm.config.modules;

import fun.bm.config.rewritten.ConfigInfo;
import fun.bm.config.rewritten.ConfigModule;

public class UnionBanConfig implements ConfigModule {

    @ConfigInfo(name = "enabled", comment = "启用UnionBan")
    public static boolean enabled = false;

    @ConfigInfo(name = "pull-only", comment = "仅拉取而不推送数据")
    public static boolean pullOnly = false;

    @ConfigInfo(name = "pull-url", comment = "UnionBan数据拉取地址")
    public static String pullUrl = "https://example.com/pull";

    @ConfigInfo(name = "push-url", comment = "UnionBan数据推送地址")
    public static String pushUrl = "https://example.com/push";

    @ConfigInfo(name = "report-key", comment = "UnionBan数据推送密钥")
    public static String reportKey = "AES-128/192/256";

    @ConfigInfo(name = "merge-period", comment = "UnionBan数据自动合并周期（玩家加入的强制同步不在此处），设置为-1为禁用")
    public static int mergePeriod = 1200;

    public String name() {
        return "union-ban";
    }
}
