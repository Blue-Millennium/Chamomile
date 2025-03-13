package fun.bm.command.main.completer.extra.sub;

import fun.bm.command.Command;
import fun.bm.util.helper.PlayerListGetter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Data extends Command.CompleterE {
    public Data() {
        super("cmdata");
    }

    public List<String> CompleteMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("query");
            completions.add("bind");
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
                                completions.addAll(PlayerListGetter.GetOnlinePlayerList());
                                break;
                            }
                            default: {
                                sender.sendMessage("Unknown command. Usage: /Chamomile data query [qq|name|uuid] [args...]");
                                break;
                            }
                        }
                    }
                    break;
                }
                case "bind": {
                    if (args.length == 2) {
                        completions.addAll(PlayerListGetter.GetOnlinePlayerList());
                    }
                }
            }
        }
        return completions;
    }
}
