package fun.blue_millennium.command.execute;

import fun.blue_millennium.config.Config;

import java.util.ArrayList;
import java.util.List;

import static fun.blue_millennium.Chamomile.LOGGER;
import static fun.blue_millennium.util.ClassesFinder.getClassesInPackage;

public class ExecutorHandler {
    public static ArrayList<Executor> executor = new ArrayList<>();

    public static ArrayList<Executor> handleExecutor() {
        List<Class<?>> extra = getClassesInPackage("fun.blue_millennium.command.execute.extra");
        List<Class<?>> vanilla = getClassesInPackage("fun.blue_millennium.command.execute.vanilla");
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
