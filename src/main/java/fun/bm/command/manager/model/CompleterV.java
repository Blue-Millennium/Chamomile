package fun.bm.command.manager.model;

import fun.bm.config.Config;
import org.jetbrains.annotations.Nullable;

public class CompleterV extends CompleterE {
    public CompleterV(@Nullable String commandName) {
        super(commandName);
        this.commandName = commandName;
    }

    public void setCommandName() {
        if (!Config.VanillaCommandsRewritten) {
            this.commandName = null;
        }
    }
}
