package fun.bm.module;

import fun.bm.util.MainEnv;
import fun.bm.util.helper.ClassLoader;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class ModuleManager {
    public ArrayList<Module> modules = new ArrayList<>();

    public void onEnable() {
        modules.forEach(module -> Bukkit.getPluginManager().registerEvents(module, MainEnv.INSTANCE));
        modules.forEach(Module::onEnable);
    }

    public void onDisable() {
        modules.forEach(Module::onDisable);
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (module.moduleName.equals(name)) {
                return module;
            }
        }
        return null;
    }

    public void setupModules(boolean setup) {
        if (!setup) onDisable();
        modules.clear();
        for (Module module : ClassLoader.loadClasses("fun.bm.module.impl", Module.class)) {
            module.setModuleName();
            if (module.getModuleName() != null) {
                modules.add(module);
            }
        }
        if (setup) {
            modules.forEach(Module::onLoad);
        } else {
            modules.forEach(Module::onReload);
            onEnable();
        }
    }
}
