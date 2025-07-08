package fun.bm.chamomile.command.modules.completer.extra.sub;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.util.helper.CommandHelper;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Check extends Command.CompleterE {
    public Check() {
        super("check");
    }

    public List<String> completerMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("verify");
            completions.add("del");
            completions.add("bind");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("bind")) {
                completions.addAll(CommandHelper.getOnlinePlayerList(args[1]));
            }
        } else if (args.length == 3) {
            completions.add("qq");
            completions.add("userid");
        }
        return completions;
    }
}
