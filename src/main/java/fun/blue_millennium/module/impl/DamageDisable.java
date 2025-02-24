package fun.blue_millennium.module.impl;

import fun.blue_millennium.module.Module;
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
}
