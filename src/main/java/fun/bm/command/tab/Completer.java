package fun.bm.command.tab;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Completer implements TabCompleter {
    public String commandName;

    public Completer(@Nullable String commandName) {
        this.commandName = commandName;
    }

    @Nullable
    public String getCommandName() {
        return this.commandName;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
