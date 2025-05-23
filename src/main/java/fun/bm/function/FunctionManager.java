package fun.bm.function;

import fun.bm.util.MainEnv;
import fun.bm.util.helper.ClassLoader;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class FunctionManager {
    public ArrayList<Function> modules = new ArrayList<>();

    public void onEnable() {
        modules.forEach(module -> Bukkit.getPluginManager().registerEvents(module, MainEnv.INSTANCE));
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
        onDisable();
        modules.clear();
        setupModules();
        modules.forEach(Function::onReload);
        onEnable();
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
        for (Function function : ClassLoader.loadClasses("fun.bm.function.modules", Function.class)) {
            function.setModuleName();
            if (function.getModuleName() != null) {
                modules.add(function);
            }
        }
    }
}
