package fun.bm.command.main.completer.extra;

import fun.bm.command.Command;
import fun.bm.command.main.completer.extra.sub.Config;
import fun.bm.command.main.completer.extra.sub.Data;
import fun.bm.command.main.completer.extra.sub.Kill;
import fun.bm.command.main.completer.extra.sub.Report;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Suisuroru
 * Date: 2024/10/15 03:01
 * function: Provides tab completion for the Chamomile command
 */
public class Chamomile extends Command.CompleterE {
    public Chamomile() {
        super("chamomile");
    }

    public List<String> CompleteMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        Config config = new Config();
        Report report = new Report();
        Data data = new Data();
        Kill kill = new Kill();
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("help");
            completions.add("report");
            completions.add("config");
            completions.add("data");
            completions.add("query-report");
            completions.add("del-report");
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
