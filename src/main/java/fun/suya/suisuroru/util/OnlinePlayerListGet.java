package fun.suya.suisuroru.util;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class OnlinePlayerListGet {
    public static List<String> GetOnlinePlayerList() {
        List<String> playerNames = new ArrayList<>();
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }
}
