package fun.bm.command.tab;

import fun.bm.config.Config;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.Chamomile.LOGGER;
import static fun.bm.util.ClassesFinder.getClassesInPackage;

public class CompleterHandler {
    public static ArrayList<Completer> completer = new ArrayList<>();

    public static ArrayList<Completer> handleCompleter() {
        List<Class<?>> extra = getClassesInPackage("fun.bm.command.tab.extra");
        List<Class<?>> vanilla = getClassesInPackage("fun.bm.command.tab.vanilla");
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
