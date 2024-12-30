package fun.suya.suisuroru.module.impl;

import fun.suya.suisuroru.config.Config;
import fun.suya.suisuroru.data.UnionBan.BanListChecker;
import fun.suya.suisuroru.module.LoadBanlist;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getServer;

public class UnionBanModule {
    public static void EnableUnionBan(JavaPlugin plugin) {
        if (Config.UnionBanEnabled) {
            getServer().getPluginManager().registerEvents(new LoadBanlist(), plugin);
            BanListChecker.scheduleDailyCheck(plugin);
        }
    }
}
