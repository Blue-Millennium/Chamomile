package fun.bm.util.helper;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandHelper {
    public static List<String> GetOnlinePlayerList() {
        List<String> playerNames = new ArrayList<>();
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }

    public static boolean checkNotOperator(CommandSender sender) {
        boolean isNotOperator = !sender.isOp();
        if (isNotOperator) {
            sender.sendMessage("您没有权限这么做");
        }
        return isNotOperator;
    }
}
