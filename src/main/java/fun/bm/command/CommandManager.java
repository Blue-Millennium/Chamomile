package fun.bm.command;

import fun.bm.util.helper.MainEnv;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;

import static fun.bm.util.helper.ClassesFinder.loadClazz;

public class CommandManager {
    public static void registerCommand() {
        registerCommands(loadClazz("fun.bm.command.executor"), (e, commandName) -> MainEnv.INSTANCE.getCommand(commandName).setExecutor((CommandExecutor) e));
        registerCommands(loadClazz("fun.bm.command.completer"), (c, commandName) -> MainEnv.INSTANCE.getCommand(commandName).setTabCompleter((TabCompleter) c));
    }

    private static <T> void registerCommands(ArrayList<Object> commands, java.util.function.BiConsumer<Object, String> commandSetter) {
        for (Object command : commands) {
            String commandName;
            commandName = ((Command.GlobalE) command).getCommandName();
            if (commandName != null) {
                commandSetter.accept(command, commandName);
            }
        }
    }
}
