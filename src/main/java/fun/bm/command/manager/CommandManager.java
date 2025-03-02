package fun.bm.command.manager;

import fun.bm.command.manager.model.CompleterE;
import fun.bm.command.manager.model.ExecutorE;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

import static fun.bm.command.manager.CommandHandler.handleCommand;

public class CommandManager {
    public static void registerCommand(JavaPlugin plugin) {
        ArrayList<ExecutorE> executor = handleCommand().getFirst();
        ArrayList<CompleterE> completer = handleCommand().getSecond();
        executor.forEach(ExecutorE::setCommandName);
        completer.forEach(CompleterE::setCommandName);
        for (ExecutorE e : executor) {
            if (e.getCommandName() != null) {
                plugin.getCommand(e.getCommandName()).setExecutor(e);
            }
        }
        for (CompleterE c : completer) {
            if (c.getCommandName() != null) {
                plugin.getCommand(c.getCommandName()).setTabCompleter(c);
            }
        }
    }
}
