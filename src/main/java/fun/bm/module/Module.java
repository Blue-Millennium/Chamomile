package fun.bm.module;

import org.bukkit.event.Listener;

public class Module implements Listener {
    public String moduleName;

    public Module(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setModuleName() {
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
