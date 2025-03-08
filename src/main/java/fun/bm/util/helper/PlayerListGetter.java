package fun.bm.util.helper;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class PlayerListGetter {
    public static List<String> GetOnlinePlayerList() {
        List<String> playerNames = new ArrayList<>();
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }
}
