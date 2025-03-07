package fun.bm.command.manager.model;

import fun.bm.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExecutorV extends ExecutorE {
    public boolean vanilla = false;

    public ExecutorV(@Nullable String commandName) {
        super(commandName);
        this.commandName = commandName;
    }

    public void setCommandName() {
        if (!Config.VanillaCommandsRewritten) {
            this.vanilla = true;
        }
    }

    public boolean vanillaCommand(CommandSender sender, String[] args) {
        return Bukkit.dispatchCommand(sender, "minecraft:" + commandName + " " + String.join(" ", args));
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (vanilla) {
            return vanillaCommand(sender, args);
        } else {
            return executeMain(sender, command, label, args);
        }
    }
}
