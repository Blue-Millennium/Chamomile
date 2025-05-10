package fun.bm.command;

import fun.bm.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Command {
    public abstract static class CompleterE extends GlobalE implements TabCompleter {
        public CompleterE(@Nullable String commandName) {
            super(commandName);
        }

        public abstract List<String> completerMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args);

        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            return completerMain(sender, command, label, args);
        }
    }

    public abstract static class CompleterV extends GlobalV implements TabCompleter {
        public CompleterV(@Nullable String commandName) {
            super(commandName);
        }

        public abstract List<String> completerMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args);

        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            return completerMain(sender, command, label, args);
        }
    }

    public abstract static class ExecutorE extends GlobalE implements CommandExecutor {

        public ExecutorE(@Nullable String commandName) {
            super(commandName);
        }

        public abstract boolean executorMain(CommandSender sender, org.bukkit.command.Command command, String label, String[] args);

        public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            return executorMain(sender, command, label, args);
        }
    }

    public abstract static class ExecutorV extends GlobalV implements CommandExecutor {
        public ExecutorV(@Nullable String commandName) {
            super(commandName);
        }

        public boolean vanillaExecutor(CommandSender sender, String[] args) {
            return Bukkit.dispatchCommand(sender, "minecraft:" + commandName + " " + String.join(" ", args));
        }

        public abstract boolean executorMain(CommandSender sender, org.bukkit.command.Command command, String label, String[] args);

        public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            if (vanilla) {
                return vanillaExecutor(sender, args);
            } else {
                return executorMain(sender, command, label, args);
            }
        }
    }

    public static class GlobalE {
        public String commandName;

        public GlobalE(@Nullable String commandName) {
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

    public static class GlobalV extends GlobalE {
        public boolean vanilla = false;

        public GlobalV(@Nullable String commandName) {
            super(commandName);
        }

        public void setupCommand() {
            if (!Config.VanillaCommandsRewritten) {
                vanilla = true;
            }
        }
    }
}
