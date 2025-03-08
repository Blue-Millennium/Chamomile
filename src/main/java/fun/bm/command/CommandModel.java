package fun.bm.command;

import fun.bm.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandModel {
    public static class CompleterE implements TabCompleter {
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

    public static class CompleterV extends CompleterE {
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
                return CompleteMain(sender, command, label, args);
            } else {
                return List.of();
            }
        }
    }

    public static class ExecutorE implements CommandExecutor {
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

        public boolean executorMain(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            return true;
        }

        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            return executorMain(sender, command, label, args);
        }
    }

    public static class ExecutorV extends ExecutorE {
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
                return executorMain(sender, command, label, args);
            }
        }
    }
}
