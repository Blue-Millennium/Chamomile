package fun.bm.chamomile.command.modules.executor.extra.sub;

import fun.bm.chamomile.command.ExtraCommand;
import fun.bm.chamomile.command.modules.executor.extra.sub.data.List;
import fun.bm.chamomile.command.modules.executor.extra.sub.data.Query;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

public class Data extends ExtraCommand implements CommandExecutor {
    Query query = new Query();
    List list = new List();

    public Data() {
        super("cmdata");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (operatorCheck(sender)) {
            return true;
        }
        if (args.length == 0) {
            if (sender instanceof Player) {
                sender.sendMessage("§e在下方的指令中，您可以使用cm来代替输入chamomile");
                sender.sendMessage("§e查询绑定数据:使用/chamomile data query <依据>");
                sender.sendMessage("§e查询绑定数据:使用/chamomile data list <起点:终点");
            } else {
                sender.sendMessage("检查到执行者为控制台，已使用前缀区分玩家指令及管理员指令");
                sender.sendMessage("在下方的指令中，您可以使用cm来代替输入chamomile");
                sender.sendMessage("[管理员/控制台]查询绑定数据:使用/chamomile data query <依据>");
                sender.sendMessage("[管理员/控制台]查询绑定数据:使用/chamomile data list <起点:终点>");
            }
        } else {
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0].toLowerCase()) {
                case "query" -> query.onCommand(sender, command, label, subArgs);
                case "list" -> list.onCommand(sender, command, label, subArgs);
                default -> sender.sendMessage("Unknown command. Usage: /chamomile data query [args...]");
            }
        }
        return true;
    }
}
