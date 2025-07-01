package fun.bm.chamomile.function.modules;

import fun.bm.chamomile.config.modules.ServerConfig;
import fun.bm.chamomile.function.Function;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageDisable extends Function {
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
        if (!ServerConfig.damageDisabled) {
            this.moduleName = null;
        }
    }
}
