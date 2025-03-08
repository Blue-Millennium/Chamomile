package fun.bm.command.completer.extra.sub;


import fun.bm.command.CommandModel;
import fun.bm.util.OnlinePlayerListGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Bind extends CommandModel.CompleterE {
    public Bind() {
        super("bind");
    }

    public List<String> CompleteMain(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(OnlinePlayerListGet.GetOnlinePlayerList());
        }
        return completions;
    }
}
