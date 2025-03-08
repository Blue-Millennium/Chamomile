package fun.bm.command;

import fun.bm.Chamomile;

import java.util.ArrayList;

import static fun.bm.util.ClassesFinder.loadClazz;

public class CommandManager {
    public static void registerCommand() {
        registerCommands(loadClazz("fun.bm.command.executor"), Command.ExecutorE.class, (e, commandName) -> Chamomile.INSTANCE.getCommand(commandName).setExecutor(e));
        registerCommands(loadClazz("fun.bm.command.completer"), Command.CompleterE.class, (c, commandName) -> Chamomile.INSTANCE.getCommand(commandName).setTabCompleter(c));
    }

    private static <T> void registerCommands(ArrayList<Object> commands, Class<T> clazz, java.util.function.BiConsumer<T, String> commandSetter) {
        for (Object command : commands) {
            T cmd = clazz.cast(command);
            String commandName = null;
            if (cmd instanceof Command.ExecutorE) {
                commandName = ((Command.ExecutorE) cmd).getCommandName();
            } else if (cmd instanceof Command.CompleterE) {
                commandName = ((Command.CompleterE) cmd).getCommandName();
            }
            if (commandName != null) {
                commandSetter.accept(cmd, commandName);
            }
        }
    }
}
