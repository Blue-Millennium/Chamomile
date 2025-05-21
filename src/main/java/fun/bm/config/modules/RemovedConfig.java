package fun.bm.config.modules;

import fun.bm.config.rewritten.ConfigInfo;
import fun.bm.config.rewritten.ConfigModule;
import fun.bm.config.rewritten.DoNotLoad;
import fun.bm.config.rewritten.TransformedConfig;

public class RemovedConfig implements ConfigModule {
    @TransformedConfig(name = "example", category = {"removed", "example"}, transform = false)
    @DoNotLoad
    @ConfigInfo(name = "removed", comment = "将不再使用的配置项重定向至此处，会自动删除配置项")
    public static boolean removed = true;

    public String name() {
        return "removed_config";
    }
}
