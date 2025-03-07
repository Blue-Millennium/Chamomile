package fun.bm.command.manager.model;

import fun.bm.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (Config.VanillaCommandsRewritten) {
            return tabCompleteMain(sender, command, label, args);
        } else {
            return List.of();
        }
    }
}
