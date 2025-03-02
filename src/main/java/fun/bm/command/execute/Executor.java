package fun.bm.command.execute;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Executor implements CommandExecutor {
    private final String commandName;

    public Executor(@Nullable String commandName) {
        this.commandName = commandName;
    }

    @Nullable
    public String getCommandName() {
        return this.commandName;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        return true;
    }
}
