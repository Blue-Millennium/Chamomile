package fun.bm.chamomile.command.modules.completer.extra.sub;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.util.helper.CommandHelper;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Data extends Command.CompleterE {
    public Data() {
        super("cmdata");
    }

    public List<String> completerMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("query");
            completions.add("list");
        } else {
            switch (args[0].toLowerCase()) {
                case "query": {
                    if (args.length == 2) {
                        completions.add("uuid");
                        completions.add("name");
                        completions.add("qq");
                        completions.add("userid");
                    } else if (args.length == 3) {
                        switch (args[1].toLowerCase()) {
                            case "uuid", "qq", "userid": {
                                break;
                            }
                            case "name": {
                                completions.addAll(CommandHelper.getOnlinePlayerList(args[2]));
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        return completions;
    }
}
