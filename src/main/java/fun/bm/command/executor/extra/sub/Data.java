package fun.bm.command.executor.extra.sub;

import fun.bm.command.executor.extra.sub.data.Bind;
import fun.bm.command.executor.extra.sub.data.Query;
import fun.bm.command.manager.model.ExecutorE;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static fun.bm.util.CommandOperatorCheck.checkNotOperator;

public class Data extends ExecutorE {
    Query query = new Query();
    Bind bind = new Bind();

    public Data() {
        super("cmdata");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender.isOp()) {
                sender.sendMessage("§e在下方的指令中，您可以使用cm来代替输入chamomile");
                sender.sendMessage("§e查询绑定数据:使用/chamomile data query <依据>");
                sender.sendMessage("§e绑定数据:使用/chamomile data bind <游戏ID> <QQ号>");
            } else {
                checkNotOperator(sender);
            }
        } else {
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
                    sender.sendMessage("Unknown command. Usage: /chamomile data query [args...]");
                    break;
                }
            }

        }
        return true;
    }
}
