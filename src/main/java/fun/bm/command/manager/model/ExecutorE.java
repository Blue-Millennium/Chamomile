package fun.bm.command.manager.model;

import org.bukkit.command.Command;
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

    public boolean executeMain(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return true;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return executeMain(sender, command, label, args);
    }
}
