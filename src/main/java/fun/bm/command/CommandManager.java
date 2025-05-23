package fun.bm.command;

import fun.bm.util.MainEnv;
import fun.bm.util.helper.ClassLoader;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public class CommandManager {
    public void registerCommands() {
        for (Command.GlobalE command : ClassLoader.loadClasses("fun.bm.command.modules", Command.GlobalE.class)) {
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
