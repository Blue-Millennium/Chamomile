package fun.bm.module.impl;

import fun.bm.config.Config;
import fun.bm.module.Module;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageDisable extends Module {

    public DamageDisable() {
        super("DamageDisable");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.getGameMode().equals(org.bukkit.GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
                event.setCancelled(true);
            }
        }
    }

    public void setModuleName() {
        if (!Config.DamageDisabled) {
            this.moduleName = null;
        }
    }
}
