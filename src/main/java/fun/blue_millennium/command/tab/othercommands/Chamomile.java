package fun.blue_millennium.command.tab.othercommands;

import fun.blue_millennium.command.tab.othercommands.sub.Config;
import fun.blue_millennium.command.tab.othercommands.sub.Data;
import fun.blue_millennium.command.tab.othercommands.sub.Kill;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Suisuroru
 * Date: 2024/10/15 03:01
 * function: Provides tab completion for the Chamomile command
 */
public class Chamomile implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Config config = new Config();
        Config report = new Config();
        Data data = new Data();
        Kill kill = new Kill();
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("help");
            completions.add("report");
            completions.add("config");
            completions.add("data");
            completions.add("query-report");
            completions.add("kill");
            completions.add("rcon");
        } else if (args.length >= 2) {
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0].toLowerCase()) {
                case "config": {
                    completions = config.onTabComplete(sender, command, label, subArgs);
                    break;
                }
                case "report": {
                    completions = report.onTabComplete(sender, command, label, subArgs);
                    break;
                }
                case "data": {
                    completions = data.onTabComplete(sender, command, label, subArgs);
                    break;
                }
                case "kill": {
                    completions = kill.onTabComplete(sender, command, label, subArgs);
                    break;
                }
                default: {
                    sender.sendMessage("Unknown command. Usage: /chamomile config [reload|query|set] [args...]");
                    break;
                }
            }
        }
        return completions;
    }
}
