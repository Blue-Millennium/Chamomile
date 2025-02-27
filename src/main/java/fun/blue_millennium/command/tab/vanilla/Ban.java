package fun.blue_millennium.command.tab.vanilla;

import fun.blue_millennium.command.tab.Completer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Ban extends Completer {

    public Ban() {
        super("ban");
    }

    public static List<String> getOnlinePlayerNames(CommandSender sender, String partialName) {
        List<String> playerNames = new ArrayList<>();
        for (Player player : sender.getServer().getOnlinePlayers()) {
            String playerName = player.getName();
            if (playerName.startsWith(partialName)) {
                playerNames.add(playerName);
            }
        }
        return playerNames;
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            // 第一个参数：玩家名称
            return getOnlinePlayerNames(sender, args[0]);
        }
        return new ArrayList<>();
    }
}
