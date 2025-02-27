package fun.blue_millennium.command;

import fun.blue_millennium.command.execute.Executor;
import fun.blue_millennium.command.tab.Completer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

import static fun.blue_millennium.command.execute.ExecutorHandler.handleExecutor;
import static fun.blue_millennium.command.tab.CompleterHandler.handleCompleter;

public class CommandManager {
    public static void registerCommand(JavaPlugin plugin) {
        ArrayList<Executor> executor = handleExecutor();
        ArrayList<Completer> completer = handleCompleter();
        for (Executor e : executor) {
            if (e.getCommandName() != null) {
                plugin.getCommand(e.getCommandName()).setExecutor(e);
            }
        }
        for (Completer c : completer) {
            if (c.getCommandName() != null) {
                plugin.getCommand(c.getCommandName()).setTabCompleter(c);
            }
        }
    }
}
