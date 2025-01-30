package fun.suya.suisuroru.commands.execute.othercommands.sub.data;

import fun.suya.suisuroru.commands.execute.othercommands.sub.data.query.DataQueryByName;
import fun.suya.suisuroru.commands.execute.othercommands.sub.data.query.DataQueryByQQ;
import fun.suya.suisuroru.commands.execute.othercommands.sub.data.query.DataQueryByUUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Query implements CommandExecutor {
    DataQueryByQQ qq = new DataQueryByQQ();
    DataQueryByName name = new DataQueryByName();
    DataQueryByUUID uuid = new DataQueryByUUID();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender.isOp()) {
                sender.sendMessage("§e在下方的指令中，您可以使用bp来代替输入baseplugin");
                sender.sendMessage("§e根据QQ号查询数据:使用/baseplugin data query qq <QQ号>");
                sender.sendMessage("§e根据玩家名字查询数据:使用/baseplugin data query name <玩家名字>");
                sender.sendMessage("§e根据UUID查询数据:使用/baseplugin data query uuid <玩家UUID>");
            } else if (!sender.isOp()) {
                sender.sendMessage("§4对不起，您没有权限执行此分支下的任何命令");
            }
        }
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        switch (args[0].toLowerCase()) {
            case "qq": {
                qq.onCommand(sender, command, label, subArgs);
                break;
            }
            case "name": {
                name.onCommand(sender, command, label, subArgs);
                break;
            }
            case "uuid": {
                uuid.onCommand(sender, command, label, subArgs);
                break;
            }
            default: {
                sender.sendMessage("Unknown command. Usage: /baseplugin data query [qq|name|uuid] [args]");
                break;
            }
        }
        return true;
    }
}
