package fun.bm.chamomile.command;

import fun.bm.chamomile.util.MainEnv;
import fun.bm.chamomile.util.helper.ClassLoadHelper;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public class CommandManager {
    public void registerCommands() {
        for (Command.GlobalE command : ClassLoadHelper.loadClasses("fun.bm.chamomile.command.modules", Command.GlobalE.class)) {
            try {
                command.setupCommand();
                String commandName = command.getCommandName();
                if (commandName != null) {
                    if (command instanceof CommandExecutor) {
                        MainEnv.INSTANCE.getCommand(commandName).setExecutor((CommandExecutor) command);
                    } else if (command instanceof TabCompleter) {
                        MainEnv.INSTANCE.getCommand(commandName).setTabCompleter((TabCompleter) command);
                    }
                }
            } catch (Exception e) {
                MainEnv.INSTANCE.getLogger().warning(e.getMessage());
            }
        }
    }
}
