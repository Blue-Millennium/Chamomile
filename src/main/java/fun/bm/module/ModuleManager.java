package fun.bm.module;

import fun.bm.Chamomile;
import fun.bm.config.Config;
import fun.bm.module.impl.*;
import org.bukkit.Bukkit;

import java.util.ArrayList;

import static fun.bm.Chamomile.LOGGER;

public class ModuleManager {
    public ArrayList<Module> modules = new ArrayList<>();

    public void load() {
        modulesSet();
        modules.forEach(Module::onLoad);
    }

    public void reload() {
        modules.clear();
        modulesSet();
        modules.forEach(Module::onReload);
    }

    public void onEnable() {
        modules.forEach(module -> Bukkit.getPluginManager().registerEvents(module, Chamomile.INSTANCE));
        modules.forEach(Module::onEnable);
    }

    public void onDisable() {
        modules.forEach(Module::onDisable);
    }

    private void modulesSet() {
        if (Config.QQRobotEnabled) {
            modules.add(new QQCheck());
            modules.add(new ExecuteRcon());
            modules.add(new SyncChat());
            modules.add(new Reporter());
        } else {
            modules.add(new DataProcess());
            LOGGER.info("QQRobot is disabled, DataProcess will be enabled.");
        }
        modules.add(new DamageDisable());
        if (Config.UnionBanEnabled) {
            modules.add(new UnionBan());
        }

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
