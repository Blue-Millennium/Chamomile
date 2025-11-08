package fun.bm.chamomile.command.modules.executor.extra.sub.check;

import fun.bm.chamomile.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.chamomile.util.data.link.LinkDataStringBuilder.buildLinkDataString;
import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

public class OpDelete extends Command.ExecutorE {
    public OpDelete() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (operatorCheck(sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§4请输入玩家名字");
        } else if (args.length == 1 || !args[1].matches("\\d+")) {
            sender.sendMessage(buildLinkDataString(sender.getName()));
            sender.sendMessage("§4请输入时间戳");
        } else {
            if (Delete.linkDataProcess(args[0], args[1])) {
                sender.sendMessage("§c尝试删除成功");
            } else {
                sender.sendMessage("§4删除失败");
            }
        }
        return true;
    }
}
