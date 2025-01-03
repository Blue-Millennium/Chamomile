package fun.suya.suisuroru.commands.tab.othercommands;

import fun.suya.suisuroru.module.impl.OnlinePlayerListGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataTab implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("query");
        } else if (args.length >= 2) {
            switch (args[1]) {
                case "query": {
                    if (args.length == 2) {
                        completions.add("uuid");
                        completions.add("name");
                        completions.add("qq");
                    } else {
                        switch (args[2]) {
                            case "uuid", "qq": {
                                break;
                            }
                            case "name": {
                                completions.addAll(OnlinePlayerListGet.GetOnlinePlayerList());
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
