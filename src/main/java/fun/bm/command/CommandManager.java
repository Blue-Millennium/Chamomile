package fun.bm.command;

import fun.bm.util.helper.MainEnv;

import java.util.ArrayList;

import static fun.bm.util.helper.ClassesFinder.loadClazz;

public class CommandManager {
    public static void registerCommand() {
        registerCommands(loadClazz("fun.bm.command.executor"), Command.ExecutorE.class, (e, commandName) -> MainEnv.INSTANCE.getCommand(commandName).setExecutor(e));
        registerCommands(loadClazz("fun.bm.command.completer"), Command.CompleterE.class, (c, commandName) -> MainEnv.INSTANCE.getCommand(commandName).setTabCompleter(c));
    }

    private static <T> void registerCommands(ArrayList<Object> commands, Class<T> clazz, java.util.function.BiConsumer<T, String> commandSetter) {
        for (Object command : commands) {
            T cmd = clazz.cast(command);
            String commandName = null;
            commandName = ((Command.Global) cmd).getCommandName();
            if (commandName != null) {
                commandSetter.accept(cmd, commandName);
            }
        }
    }
}
