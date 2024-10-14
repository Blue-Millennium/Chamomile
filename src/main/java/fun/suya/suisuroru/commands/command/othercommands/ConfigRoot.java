package fun.suya.suisuroru.commands.command.othercommands;

import fun.suya.suisuroru.commands.command.othercommands.config.Query;
import fun.suya.suisuroru.commands.command.othercommands.config.Reload;
import fun.suya.suisuroru.commands.command.othercommands.config.Set;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Suisuroru
 * Date: 2024/10/14 22:41
 * function: Config settings
 */
public class ConfigRoot implements CommandExecutor {

    Reload reload = new Reload();
    Query query = new Query();
    Set set = new Set();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (command.getName().equals("bp")) {
                sender.sendMessage("§c/bp config [reload|query|set] [args...]");
            } else if (command.getName().equals("baseplugin")) {
                sender.sendMessage("§c/baseplugin config [reload|query|set] [args...]");
            }
            return true;
        }

        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        switch (args[0].toLowerCase()) {
            case "reload": {
                reload.onCommand(sender, command, label, subArgs);
                break;
            }
            case "query": {
                query.onCommand(sender, command, label, subArgs);
                break;
            }
            case "set": {
                set.onCommand(sender, command, label, subArgs);
                break;
            }
            default: {
                sender.sendMessage("Unknown command. Usage: /baseplugin config [reload|query|set] [args...]");
                break;
            }
        }
        return true;
    }
}