package fun.bm.chamomile.command.modules.completer.extra.sub;


import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.util.MainEnv;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Suisuroru
 * Date: 2024/10/18 22:11
 * function: Provides tab completion for the config command
 */
public class Config extends Command.CompleterE {
    public Config() {
        super("cmconfig");
    }

    public List<String> completerMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("reload");
            completions.add("query");
            completions.add("set");
        } else if (args.length == 2) {
            if (args[0].equals("query") || args[0].equals("set")) {
                completions.addAll(MainEnv.configManager.completeConfigPath(args[1]));
            }
        }
        return completions;
    }
}
