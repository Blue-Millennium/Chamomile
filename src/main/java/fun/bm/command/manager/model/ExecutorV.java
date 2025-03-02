package fun.bm.command.manager.model;

import fun.bm.config.Config;
import org.jetbrains.annotations.Nullable;

public class ExecutorV extends ExecutorE {
    public ExecutorV(@Nullable String commandName) {
        super(commandName);
        this.commandName = commandName;
    }

    public void setCommandName() {
        if (Config.VanillaCommandsRewritten) {
            this.commandName = null;
        }
    }
}
