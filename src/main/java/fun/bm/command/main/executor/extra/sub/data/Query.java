package fun.bm.command.main.executor.extra.sub.data;

import fun.bm.command.Command;
import fun.bm.command.main.executor.extra.sub.data.query.DataQueryByName;
import fun.bm.command.main.executor.extra.sub.data.query.DataQueryByQQ;
import fun.bm.command.main.executor.extra.sub.data.query.DataQueryByUUID;
import fun.bm.command.main.executor.extra.sub.data.query.DataQueryByUserID;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static fun.bm.util.helper.OperatorChecker.checkNotOperator;

public class Query extends Command.ExecutorE {
    DataQueryByQQ qq = new DataQueryByQQ();
    DataQueryByName name = new DataQueryByName();
    DataQueryByUUID uuid = new DataQueryByUUID();
    DataQueryByUserID userid = new DataQueryByUserID();

    public Query() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender.isOp()) {
                sender.sendMessage("§e在下方的指令中，您可以使用cm来代替输入chamomile");
                sender.sendMessage("§e根据QQ号查询数据:使用/chamomile data query qq <QQ号>");
                sender.sendMessage("§e根据玩家名字查询数据:使用/chamomile data query name <玩家名字>");
                sender.sendMessage("§e根据UUID查询数据:使用/chamomile data query uuid <玩家UUID>");
            } else if (checkNotOperator(sender)) {
                return true;
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
            case "userid": {
                userid.onCommand(sender, command, label, subArgs);
                break;
            }
            default: {
                sender.sendMessage("Unknown command. Usage: /chamomile data query [qq|name|uuid] [args]");
                break;
            }
        }
        return true;
    }
}
