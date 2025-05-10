package fun.bm.command.main.completer.vanilla;

import fun.bm.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Ban extends Command.CompleterV {

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

    public List<String> completerMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            // 第一个参数：玩家名称
            return getOnlinePlayerNames(sender, args[0]);
        }
        return new ArrayList<>();
    }
}
