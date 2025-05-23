package fun.bm.function;

import org.bukkit.event.Listener;

public class Function implements Listener {
    public String moduleName;

    public Function(String moduleName) {
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
