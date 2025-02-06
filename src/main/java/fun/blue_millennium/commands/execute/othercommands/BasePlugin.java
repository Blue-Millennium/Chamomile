package fun.blue_millennium.commands.execute.othercommands;

import fun.blue_millennium.commands.execute.othercommands.sub.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Suisuroru
 * Date: 2024/9/28 13:43
 * function: Manager command in baseplugin
 */
public class BasePlugin implements CommandExecutor {

    Report report = new Report();
    Config config = new Config();
    ReportQuery query = new ReportQuery();
    Help help = new Help();
    Data data = new Data();
    Kill kill = new Kill();
    Rcon rcon = new Rcon();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (command.getName().equals("bp")) {
                sender.sendMessage("§c使用/bp help来获取帮助");
            } else if (command.getName().equals("baseplugin")) {
                sender.sendMessage("§c使用/baseplugin help来获取帮助");
            }
            return true;
        }

        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        switch (args[0].toLowerCase()) {
            case "help": {
                help.onCommand(sender, command, label, subArgs);
                break;
            }
            case "report": {
                report.onCommand(sender, command, label, subArgs);
                break;
            }
            case "config": {
                config.onCommand(sender, command, label, subArgs);
                break;
            }
            case "query-report": {
                query.onCommand(sender, command, label, subArgs);
                break;
            }
            case "data": {
                data.onCommand(sender, command, label, subArgs);
                break;
            }
            case "kill": {
                kill.onCommand(sender, command, label, subArgs);
                break;
            }
            case "rcon": {
                rcon.onCommand(sender, command, label, subArgs);
                break;
            }
            default: {
                sender.sendMessage("Unknown command. Usage: /baseplugin [report|reload|config|data|query-report] [args...]");
                break;
            }
        }
        return true;
    }
}
