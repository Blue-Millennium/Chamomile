package fun.bm.config.modules;

import fun.bm.config.rewritten.ConfigInfo;
import fun.bm.config.rewritten.ConfigModule;
import fun.bm.config.rewritten.DoNotLoad;
import fun.bm.config.rewritten.TransformedConfig;

public class RemovedConfig implements ConfigModule {
    @TransformedConfig(name = "example", category = {"removed", "example"}, transform = false)
    @DoNotLoad
    @ConfigInfo(name = "removed", comment =
            """
                    RemovedConfig redirect to here, no any function.""")
    public static boolean removed = true;

    public String name() {
        return "removed_config";
    }
}
