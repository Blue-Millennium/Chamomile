package fun.bm.command.manager;

import fun.bm.Chamomile;
import fun.bm.command.manager.model.CompleterE;
import fun.bm.command.manager.model.ExecutorE;

import java.util.ArrayList;

import static fun.bm.util.ClassesFinder.loadClazz;

public class CommandManager {
    public static void registerCommand() {
        ArrayList<Object> executor = loadClazz("fun.bm.command.executor");
        ArrayList<Object> completer = loadClazz("fun.bm.command.completer");
        for (Object e : executor) {
            ExecutorE e1 = (ExecutorE) e;
            e1.setCommandName();
            if (e1.getCommandName() != null) {
                Chamomile.INSTANCE.getCommand(e1.getCommandName()).setExecutor(e1);
            }
        }
        for (Object c : completer) {
            CompleterE c1 = (CompleterE) c;
            c1.setCommandName();
            if (c1.getCommandName() != null) {
                Chamomile.INSTANCE.getCommand(c1.getCommandName()).setTabCompleter(c1);
            }
        }
    }
}
