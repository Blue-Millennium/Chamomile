package fun.bm.chamomile.command.modules.completer.extra.sub;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.util.Environment;
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
            switch (args[0].toLowerCase()) {
                case "bind", "opdel" -> completions.addAll(CommandHelper.getOnlinePlayerList(args[1]));
                case "del" ->
                        Environment.dataManager.baseDataManager.getPlayerDataByName(sender.getName()).linkData.forEach(linkData -> completions.add(String.valueOf(linkData.linkedTime)));
            }
        } else if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "bind" -> {
                    completions.add("qq");
                    completions.add("userid");
                }
                case "opdel" -> {
                    Environment.dataManager.baseDataManager.getPlayerDataByName(args[0]).linkData.forEach(linkData -> completions.add(String.valueOf(linkData.linkedTime)));
                }
            }
        }
        return completions;
    }
}
