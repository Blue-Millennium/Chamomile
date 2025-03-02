package fun.bm.command.execute;

import fun.bm.config.Config;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.Chamomile.LOGGER;
import static fun.bm.util.ClassesFinder.getClassesInPackage;

public class ExecutorHandler {
    public static ArrayList<Executor> executor = new ArrayList<>();

    public static ArrayList<Executor> handleExecutor() {
        List<Class<?>> extra = getClassesInPackage("fun.bm.command.execute.extra");
        List<Class<?>> vanilla = getClassesInPackage("fun.bm.command.execute.vanilla");
        for (Class<?> clazz : extra) {
            try {
                executor.add((Executor) clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                LOGGER.warning("Failed to register command: " + clazz.getName());
            }
        }
        if (Config.VanillaCommandsRewritten) {
            for (Class<?> clazz : vanilla) {
                try {
                    executor.add((Executor) clazz.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    LOGGER.warning("Failed to register command: " + clazz.getName());
                }
            }
        }
        return executor;
    }
}
