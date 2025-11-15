package fun.bm.chamomile.command.modules.extra;

import fun.bm.chamomile.command.ExtraCommand;
import fun.bm.chamomile.command.modules.extra.sub.*;
import fun.bm.chamomile.command.modules.extra.sub.report.ReportDelete;
import fun.bm.chamomile.command.modules.extra.sub.report.ReportQuery;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Suisuroru
 * Date: 2024/9/28 13:43
 * function: Manager command in Chamomile
 */
public class Chamomile extends ExtraCommand implements CommandExecutor, TabCompleter {

    Report report = new Report();
    ReportQuery query = new ReportQuery();
    ReportDelete del = new ReportDelete();
    Config config = new Config();
    Help help = new Help();
    Data data = new Data();
    Check check = new Check();
    Kill kill = new Kill();

    public Chamomile() {
        super("chamomile");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("在下方的指令中，您可以使用cm来代替输入chamomile");
            sender.sendMessage("§c使用/chamomile help来获取帮助");
        } else {
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0].toLowerCase()) {
                case "help" -> help.onCommand(sender, command, label, subArgs);
                case "report" -> report.onCommand(sender, command, label, subArgs);
                case "query-report" -> query.onCommand(sender, command, label, subArgs);
                case "del-report" -> del.onCommand(sender, command, label, subArgs);
                case "config" -> config.onCommand(sender, command, label, subArgs);
                case "data" -> data.onCommand(sender, command, label, subArgs);
                case "kill" -> kill.onCommand(sender, command, label, subArgs);
                case "check" -> check.onCommand(sender, command, label, subArgs);
                default ->
                        sender.sendMessage("Unknown command. Usage: /chamomile [report|reload|config|data|check|kill|query-report|del-report] [args...]");
            }
        }
        return true;
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
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
