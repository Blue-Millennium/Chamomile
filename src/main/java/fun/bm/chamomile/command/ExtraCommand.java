package fun.bm.chamomile.command;

import org.jetbrains.annotations.Nullable;

public class ExtraCommand {
    public String commandName;

    public ExtraCommand(@Nullable String commandName) {
        this.commandName = commandName;
    }

    @Nullable
    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public void setupCommand() {
    }
}
