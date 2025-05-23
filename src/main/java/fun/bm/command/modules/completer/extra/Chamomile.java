package fun.bm.command.modules.completer.extra;

import fun.bm.command.Command;
import fun.bm.command.modules.completer.extra.sub.*;
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
    Config config = new Config();
    Report report = new Report();
    Data data = new Data();
    Kill kill = new Kill();
    Check check = new Check();

    public Chamomile() {
        super("chamomile");
    }

    public List<String> completerMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("help");
            completions.add("report");
            completions.add("config");
            completions.add("data");
            completions.add("query-report");
            completions.add("del-report");
            completions.add("kill");
        } else if (args.length >= 2) {
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0].toLowerCase()) {
                case "config" -> completions = config.onTabComplete(sender, command, label, subArgs);
                case "report" -> completions = report.onTabComplete(sender, command, label, subArgs);
                case "data" -> completions = data.onTabComplete(sender, command, label, subArgs);
                case "check" -> completions = check.onTabComplete(sender, command, label, subArgs);
                case "kill" -> completions = kill.onTabComplete(sender, command, label, subArgs);
            }
        }
        return completions;
    }
}
