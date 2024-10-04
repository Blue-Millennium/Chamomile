package fun.suya.suisuroru.command;

import fun.suya.suisuroru.command.othercommands.ReloadConfig;
import fun.suya.suisuroru.command.othercommands.ReportQuery;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xd.suka.command.ReportCommand;

/**
 * @author Suisuroru
 * Date: 2024/9/28 13:43
 * function: Save data of report
 */
public class CommandManager implements CommandExecutor {

    ReportCommand report = new ReportCommand();
    ReloadConfig reload = new ReloadConfig();
    ReportQuery query = new ReportQuery();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /baseplugin [report|reload|query-report] [args...]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "report":
                String[] reportArgs = new String[args.length - 1];
                System.arraycopy(args, 1, reportArgs, 0, args.length - 1);
                report.onCommand(sender, command, label, reportArgs);
                break;
            case "reload":
                String[] reloadArgs = new String[args.length - 1];
                System.arraycopy(args, 1, reloadArgs, 0, args.length - 1);
                reload.onCommand(sender, command, label, reloadArgs);
                break;
            case "query-report":
                String[] queryArgs = new String[args.length - 1];
                System.arraycopy(args, 1, queryArgs, 0, args.length - 1);
                query.onCommand(sender, command, label, queryArgs);
                break;
            default:
                sender.sendMessage("Unknown command. Usage: /baseplugin [report|reload|query-report] [args...]");
                return true;
        }
        return true;
    }
}
