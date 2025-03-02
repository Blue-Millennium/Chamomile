package fun.bm.command.manager.model;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExecutorE implements CommandExecutor {
    String commandName;

    public ExecutorE(@Nullable String commandName) {
        this.commandName = commandName;
    }

    public void setCommandName() {
    }

    @Nullable
    public String getCommandName() {
        return this.commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        return true;
    }
}
