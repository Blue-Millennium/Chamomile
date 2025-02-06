package fun.blue_millennium.commands.execute.othercommands.sub;

import fun.blue_millennium.commands.execute.othercommands.sub.data.Bind;
import fun.blue_millennium.commands.execute.othercommands.sub.data.Query;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Data implements CommandExecutor {
    Query query = new Query();
    Bind bind = new Bind();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender.isOp()) {
                sender.sendMessage("§e在下方的指令中，您可以使用bp来代替输入baseplugin");
                sender.sendMessage("§e查询绑定数据:使用/baseplugin data query <依据>");
            } else if (!sender.isOp()) {
                sender.sendMessage("§4对不起，您没有权限执行此分支下的任何命令");
            }
        }
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        switch (args[0].toLowerCase()) {
            case "query": {
                query.onCommand(sender, command, label, subArgs);
                break;
            }
            case "bind": {
                bind.onCommand(sender, command, label, subArgs);
                break;
            }
            default: {
                sender.sendMessage("Unknown command. Usage: /baseplugin data query [args...]");
                break;
            }
        }
        return true;
    }
}
