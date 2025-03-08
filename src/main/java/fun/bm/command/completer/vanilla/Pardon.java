package fun.bm.command.completer.vanilla;

import fun.bm.command.CommandModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Pardon extends CommandModel.CompleterV {
    public Pardon() {
        super("pardon");
    }

    public List<String> CompleteMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, String[] args) {
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
