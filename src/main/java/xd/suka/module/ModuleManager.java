package xd.suka.module;

import fun.suya.suisuroru.module.impl.RconPreCheck;
import org.bukkit.Bukkit;
import xd.suka.Main;
import xd.suka.module.impl.QQCheck;
import xd.suka.module.impl.Reporter;
import xd.suka.module.impl.SyncChat;

import java.util.ArrayList;

public class ModuleManager {
    public ArrayList<Module> modules = new ArrayList<>();

    public void load() {
        modules.add(new SyncChat());
        modules.add(new QQCheck());
        modules.add(new Reporter());
        modules.add(new RconPreCheck());

        modules.forEach(Module::onLoad);
    }

    public void onEnable() {
        modules.forEach(module -> Bukkit.getPluginManager().registerEvents(module, Main.INSTANCE));
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
}
