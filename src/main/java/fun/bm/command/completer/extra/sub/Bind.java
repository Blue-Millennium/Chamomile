package fun.bm.command.completer.extra.sub;


import fun.bm.command.Command;
import fun.bm.util.helper.PlayerListGetter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Bind extends Command.CompleterE {
    public Bind() {
        super("bind");
    }

    public List<String> CompleteMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(PlayerListGetter.GetOnlinePlayerList());
        }
        return completions;
    }
}
