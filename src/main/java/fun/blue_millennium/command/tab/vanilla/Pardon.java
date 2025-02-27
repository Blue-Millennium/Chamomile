package fun.blue_millennium.command.tab.vanilla;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Pardon implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], getBannedPlayerNames(), new ArrayList<>());
        }
        return new ArrayList<>();
    }

    private List<String> getBannedPlayerNames() {
        List<String> bannedPlayerNames = new ArrayList<>();
        for (OfflinePlayer bannedPlayer : Bukkit.getBannedPlayers()) {
            bannedPlayerNames.add(bannedPlayer.getName());
        }
        return bannedPlayerNames;
    }
}
