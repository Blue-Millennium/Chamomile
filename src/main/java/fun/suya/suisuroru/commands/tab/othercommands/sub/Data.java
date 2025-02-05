package fun.suya.suisuroru.commands.tab.othercommands.sub;

import fun.suya.suisuroru.module.impl.OnlinePlayerListGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Data implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("query");
            completions.add("bind");
        } else if (args.length >= 2) {
            switch (args[0].toLowerCase()) {
                case "query": {
                    if (args.length == 2) {
                        completions.add("uuid");
                        completions.add("name");
                        completions.add("qq");
                    } else if (args.length == 3) {
                        switch (args[1].toLowerCase()) {
                            case "uuid", "qq": {
                                break;
                            }
                            case "name": {
                                completions.addAll(OnlinePlayerListGet.GetOnlinePlayerList());
                                break;
                            }
                            default: {
                                sender.sendMessage("Unknown command. Usage: /baseplugin data query [qq|name|uuid] [args...]");
                                break;
                            }
                        }
                    }
                    break;
                }
                case "bind": {
                    if (args.length == 2) {
                        completions.addAll(OnlinePlayerListGet.GetOnlinePlayerList());
                    }
                }
            }
        }
        return completions;
    }
}
