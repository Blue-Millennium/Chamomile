package fun.bm.command.manager.model;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CompleterE implements TabCompleter {
    public String commandName;

    public CompleterE(@Nullable String commandName) {
        this.commandName = commandName;
    }

    @Nullable
    public String getCommandName() {
        return this.commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public void setCommandName() {
    }

    public List<String> CompleteMain(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return CompleteMain(sender, command, label, args);
    }
}
