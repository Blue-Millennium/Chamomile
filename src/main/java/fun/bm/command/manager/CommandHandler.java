package fun.bm.command.manager;

import fun.bm.command.manager.model.CompleterE;
import fun.bm.command.manager.model.ExecutorE;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.Chamomile.LOGGER;
import static fun.bm.util.ClassesFinder.getClassesInPackage;

public class CommandHandler {
    public static ArrayList<ExecutorE> executor = new ArrayList<>();
    public static ArrayList<CompleterE> completer = new ArrayList<>();

    public static Pair<ArrayList<ExecutorE>, ArrayList<CompleterE>> handleCommand() {
        List<Class<?>> executor = getClassesInPackage("fun.bm.command.executor");
        List<Class<?>> completer = getClassesInPackage("fun.bm.command.completer");
        for (Class<?> clazz : executor) {
            try {
                CommandHandler.executor.add((ExecutorE) clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                LOGGER.warning("Failed to register command: " + clazz.getName());
            }
        }
        for (Class<?> clazz : completer) {
            try {
                CommandHandler.completer.add((CompleterE) clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                LOGGER.warning("Failed to register command: " + clazz.getName());
            }
        }
        return new Pair<>(CommandHandler.executor, CommandHandler.completer);
    }
}
