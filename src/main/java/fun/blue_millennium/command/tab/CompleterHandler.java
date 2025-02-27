package fun.blue_millennium.command.tab;

import fun.blue_millennium.config.Config;

import java.util.ArrayList;
import java.util.List;

import static fun.blue_millennium.Chamomile.LOGGER;
import static fun.blue_millennium.util.ClassesFinder.getClassesInPackage;

public class CompleterHandler {
    public static ArrayList<Completer> completer = new ArrayList<>();

    public static ArrayList<Completer> handleCompleter() {
        List<Class<?>> extra = getClassesInPackage("fun.blue_millennium.command.tab.extra");
        List<Class<?>> vanilla = getClassesInPackage("fun.blue_millennium.command.tab.vanilla");
        for (Class<?> clazz : extra) {
            try {
                completer.add((Completer) clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                LOGGER.warning("Failed to register command: " + clazz.getName());
            }
        }
        if (Config.VanillaCommandsRewritten) {
            for (Class<?> clazz : vanilla) {
                try {
                    completer.add((Completer) clazz.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    LOGGER.warning("Failed to register command: " + clazz.getName());
                }
            }
        }
        return completer;
    }
}
