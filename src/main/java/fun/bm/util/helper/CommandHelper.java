package fun.bm.util.helper;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandHelper {
    public static List<String> getOnlinePlayerList(String partialName) {
        List<String> playerNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            String playerName = player.getName();
            if (playerName.startsWith(partialName)) {
                playerNames.add(playerName);
            }
        }
        return playerNames;
    }

    public static boolean operatorCheck(CommandSender sender) {
        boolean isNotOperator = !sender.isOp();
        if (isNotOperator) {
            sender.sendMessage("您没有权限这么做");
        }
        return isNotOperator;
    }
}
