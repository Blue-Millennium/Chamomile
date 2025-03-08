package fun.bm.module;

import fun.bm.Chamomile;
import org.bukkit.Bukkit;

import java.util.ArrayList;

import static fun.bm.util.helper.ClassesFinder.loadClazz;

public class ModuleManager {
    public ArrayList<Module> modules = new ArrayList<>();

    public void onEnable() {
        modules.forEach(module -> Bukkit.getPluginManager().registerEvents(module, Chamomile.INSTANCE));
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

    public void setupModules(Boolean setup) {
        modules.clear();
        for (Object clazz : loadClazz("fun.bm.module.impl")) {
            Module module = (Module) clazz;
            module.setModuleName();
            if (module.getModuleName() != null) {
                modules.add(module);
                if (setup) {
                    module.onLoad();
                } else {
                    module.onReload();
                }
            }
        }
    }
}
