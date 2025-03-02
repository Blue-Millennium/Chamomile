package fun.bm.command;

import fun.bm.command.execute.Executor;
import fun.bm.command.tab.Completer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

import static fun.bm.command.execute.ExecutorHandler.handleExecutor;
import static fun.bm.command.tab.CompleterHandler.handleCompleter;

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
