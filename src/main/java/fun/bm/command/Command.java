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
    public static class CompleterE extends GlobalE implements TabCompleter {
        public CompleterE(@Nullable String commandName) {
            super(commandName);
        }

        public List<String> CompleteMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            return List.of();
        }

        @Nullable
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            return CompleteMain(sender, command, label, args);
        }
    }

    public static class CompleterV extends GlobalV implements TabCompleter {
        public CompleterV(@Nullable String commandName) {
            super(commandName);
        }

        public List<String> CompleteMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            return List.of();
        }

        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            if (vanilla) {
                return CompleteMain(sender, command, label, args);
            } else {
                return List.of();
            }
        }
    }

    public static class ExecutorE extends GlobalE implements CommandExecutor {

        public ExecutorE(@Nullable String commandName) {
            super(commandName);
        }

        public boolean executorMain(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            return true;
        }

        public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            return executorMain(sender, command, label, args);
        }
    }

    public static class ExecutorV extends GlobalV implements CommandExecutor {
        public ExecutorV(@Nullable String commandName) {
            super(commandName);
        }

        public boolean vanillaCommand(CommandSender sender, String[] args) {
            return Bukkit.dispatchCommand(sender, "minecraft:" + commandName + " " + String.join(" ", args));
        }

        public boolean executorMain(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            return true;
        }

        public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            if (vanilla) {
                return vanillaCommand(sender, args);
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
