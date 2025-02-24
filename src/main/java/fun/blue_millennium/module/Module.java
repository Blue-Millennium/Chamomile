package fun.blue_millennium.module;

import org.bukkit.event.Listener;

public class Module implements Listener {
    public String moduleName;

    public Module(String moduleName) {
        this.moduleName = moduleName;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onLoad() {
    }

    public void onReload() {
        onLoad();
    }
}
