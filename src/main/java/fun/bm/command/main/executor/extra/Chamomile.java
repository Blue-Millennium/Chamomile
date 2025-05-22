package fun.bm.command.main.executor.extra;

import fun.bm.command.Command;
import fun.bm.command.main.executor.extra.sub.*;
import fun.bm.command.main.executor.extra.sub.report.Report;
import fun.bm.command.main.executor.extra.sub.report.ReportDelete;
import fun.bm.command.main.executor.extra.sub.report.ReportQuery;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Suisuroru
 * Date: 2024/9/28 13:43
 * function: Manager command in Chamomile
 */
public class Chamomile extends Command.ExecutorE {

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

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
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
}
