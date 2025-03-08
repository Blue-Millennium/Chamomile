package fun.bm.command.executor.extra.sub;

import fun.bm.command.manager.model.ExecutorE;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static fun.bm.util.CommandOperatorCheck.checkNotOperator;

public class Kill extends ExecutorE {
    public Kill() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (checkNotOperator(sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§4请输入玩家名");
        } else {
            Player target = sender.getServer().getPlayer(args[0]);
            if (target != null && target.isOnline()) {
                target.setHealth(0);
                Bukkit.broadcastMessage("§4Chamomile已尝试清除玩家: " + args[0]);
            } else {
                sender.sendMessage("§4玩家不在线或不存在");
            }
        }
        return true;
    }
}
