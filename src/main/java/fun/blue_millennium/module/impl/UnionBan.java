package fun.blue_millennium.module.impl;

import fun.blue_millennium.config.Config;
import fun.blue_millennium.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UnionBan extends Module implements Listener {

    public UnionBan() {
        super("UnionBan");
    }

    @EventHandler
    public void PlayerJoinMessage(PlayerJoinEvent event) {
        if (Config.UnionBanEnabled) {

        }
    }
}