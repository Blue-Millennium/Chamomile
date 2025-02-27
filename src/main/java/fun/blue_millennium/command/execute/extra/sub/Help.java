package fun.blue_millennium.command.execute.extra.sub;

import fun.blue_millennium.command.execute.Executor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Suisuroru
 * Date: 2024/10/10 20:30
 * function: Show help to players
 */
public class Help extends Executor {

    public Help() {
        super("chamomilehelp");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (sender.isOp()) {
                sender.sendMessage("§e检查到执行者为OP，已使用颜色区分需要管理员权限的指令，绿色为通用指令，红色为管理员权限指令");
                sender.sendMessage("§e在下方的指令中，您可以使用cm来代替输入chamomile");
                sender.sendMessage("§a举报玩家:/chamomile report <玩家名> <原因>");
                sender.sendMessage("§4查询举报记录:/chamomile query-report");
                sender.sendMessage("§4配置文件处理:使用/chamomile config获取更多帮助");
                sender.sendMessage("§4验证数据处理:使用/chamomile data获取更多帮助");
                sender.sendMessage("§4异常玩家kill:使用/chamomile kill <玩家名>");
            } else if (!sender.isOp()) {
                sender.sendMessage("§a举报玩家:/chamomile report <玩家名> <原因>");
            }
        } else {
            sender.sendMessage("检查到执行者为控制台，已使用前缀区分玩家指令及管理员指令");
            sender.sendMessage("在下方的指令中，您可以使用cm来代替输入chamomile");
            sender.sendMessage("[玩家]举报玩家:/chamomile report <玩家名> <原因>");
            sender.sendMessage("[管理员/控制台]查询举报记录:/chamomile query-report");
            sender.sendMessage("[管理员/控制台]配置文件处理:使用/chamomile config获取更多帮助");
            sender.sendMessage("[管理员/控制台]验证数据处理:使用/chamomile data获取更多帮助");
            sender.sendMessage("[管理员/控制台]异常玩家kill:使用/chamomile kill <玩家名>");
        }
        return true;
    }
}
