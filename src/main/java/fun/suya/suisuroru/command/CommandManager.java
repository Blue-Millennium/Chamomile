package fun.suya.suisuroru.command;

import fun.suya.suisuroru.command.othercommands.ReloadConfig;
import fun.suya.suisuroru.command.othercommands.ReportQuery;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xd.suka.command.ReportCommand;

import java.util.Arrays;

/**
 * @author Suisuroru
 * Date: 2024/9/28 13:43
 * function: Manager command in baseplugin
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

        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        switch (args[0].toLowerCase()) {
            case "report": {
                report.onCommand(sender, command, label, subArgs);
                break;
            }
            case "reload": {
                reload.onCommand(sender, command, label, subArgs);
                break;
            }
            case "query-report": {
                query.onCommand(sender, command, label, subArgs);
                break;
            }
            default: {
                sender.sendMessage("Unknown command. Usage: /baseplugin [report|reload|query-report] [args...]");
                break;
            }
        }
        return true;
    }
}
