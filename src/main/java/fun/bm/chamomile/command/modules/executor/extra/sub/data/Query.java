package fun.bm.chamomile.command.modules.executor.extra.sub.data;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.command.modules.executor.extra.sub.data.query.DataQueryByName;
import fun.bm.chamomile.command.modules.executor.extra.sub.data.query.DataQueryByQQ;
import fun.bm.chamomile.command.modules.executor.extra.sub.data.query.DataQueryByUUID;
import fun.bm.chamomile.command.modules.executor.extra.sub.data.query.DataQueryByUserID;
import fun.bm.chamomile.util.data.DataGet;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

public class Query extends Command.ExecutorE {
    public static DataGet dataGet = new DataGet();
    DataQueryByQQ qq = new DataQueryByQQ();
    DataQueryByName name = new DataQueryByName();
    DataQueryByUUID uuid = new DataQueryByUUID();
    DataQueryByUserID userid = new DataQueryByUserID();

    public Query() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (operatorCheck(sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§e在下方的指令中，您可以使用cm来代替输入chamomile");
            sender.sendMessage("§e根据QQ号查询数据:使用/chamomile data query qq <QQ号>");
            sender.sendMessage("§e根据Userid查询数据:使用/chamomile data query userid <userid>");
            sender.sendMessage("§e根据玩家名字查询数据:使用/chamomile data query name <玩家名字>");
            sender.sendMessage("§e根据UUID查询数据:使用/chamomile data query uuid <玩家UUID>");
        }
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        switch (args[0].toLowerCase()) {
            case "qq" -> qq.onCommand(sender, command, label, subArgs);
            case "name" -> name.onCommand(sender, command, label, subArgs);
            case "uuid" -> uuid.onCommand(sender, command, label, subArgs);
            case "userid" -> userid.onCommand(sender, command, label, subArgs);
            default -> sender.sendMessage("Unknown command. Usage: /chamomile data query [qq|name|uuid|userid] [args]");
        }
        return true;
    }
}
