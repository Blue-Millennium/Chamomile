package fun.bm.command;

import fun.bm.util.MainEnv;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import static fun.bm.util.helper.ClassLoader.loadClazz;

public class CommandManager {
    public void registerCommands() {
        for (Object command : loadClazz("fun.bm.command.main")) {
            try {
                String commandName;
                commandName = ((Command.GlobalE) command).getCommandName();
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
