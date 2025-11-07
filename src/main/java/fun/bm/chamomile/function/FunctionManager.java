package fun.bm.chamomile.function;

import fun.bm.chamomile.util.Environment;
import fun.bm.chamomile.util.helper.ClassLoadHelper;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class FunctionManager {
    public ArrayList<Function> modules = new ArrayList<>();

    public void onEnable() {
        modules.forEach(module -> Bukkit.getPluginManager().registerEvents(module, Environment.INSTANCE));
        modules.forEach(Function::onEnable);
    }

    public void onDisable() {
        modules.forEach(Function::onDisable);
    }

    public void load() {
        setupModules();
        modules.forEach(Function::onLoad);
    }

    public void reload() {
        setupModules();
        modules.forEach(Function::onReload);
        onEnable();
    }

    public void preReload() {
        onDisable();
        modules.clear();
    }

    public Function getModuleByName(String name) {
        for (Function function : modules) {
            if (function.moduleName.equals(name)) {
                return function;
            }
        }
        return null;
    }

    public void setupModules() {
        for (Function function : ClassLoadHelper.loadClasses("fun.bm.chamomile.function.modules", Function.class)) {
            function.setModuleName();
            if (function.getModuleName() != null) {
                modules.add(function);
            }
        }
    }
}
