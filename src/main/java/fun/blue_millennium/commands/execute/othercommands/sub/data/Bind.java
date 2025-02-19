package fun.blue_millennium.commands.execute.othercommands.sub.data;

import fun.blue_millennium.Chamomile;
import fun.blue_millennium.data.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.blue_millennium.Chamomile.LOGGER;
import static fun.blue_millennium.util.CommandOperatorCheck.checkNotOperator;

public class Bind implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (checkNotOperator(sender)) {
            return true;
        }
        Data data = Chamomile.INSTANCE.dataManager.getPlayerDataByName(args[0]);
        if (data == null) {
            LOGGER.warning("§4Some errors occur in data processing.");
            return true;
        } else {
            data.qqNumber = Long.parseLong(args[1]);
            data.linkedTime = System.currentTimeMillis();
            data.qqChecked = true;
            Chamomile.INSTANCE.dataManager.setPlayerDataByName(args[0], data);
            sender.sendMessage("§e已将游戏名为" + args[0] + "的玩家绑定QQ为" + args[1]);
        }
        return true;
    }
}
