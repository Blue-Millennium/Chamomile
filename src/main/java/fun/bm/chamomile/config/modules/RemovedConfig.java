package fun.bm.chamomile.config.modules;

import fun.bm.chamomile.config.ConfigModule;
import fun.bm.chamomile.config.flags.ConfigInfo;
import fun.bm.chamomile.config.flags.TransformedConfig;

public class RemovedConfig implements ConfigModule {
    @TransformedConfig(name = "example", category = {"removed", "example"}, transform = false)
    @ConfigInfo(name = "removed", comment = "将不再使用的配置项重定向至此处，会自动删除配置项")
    public static boolean removed = true;

    public String name() {
        return "removed_config";
    }
}
