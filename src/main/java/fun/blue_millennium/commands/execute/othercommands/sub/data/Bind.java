package fun.blue_millennium.commands.execute.othercommands.sub.data;

import fun.blue_millennium.Main;
import fun.blue_millennium.data.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.blue_millennium.Main.LOGGER;

public class Bind implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("您没有权限这么做");
            return true;
        }
        Data data = Main.INSTANCE.dataManager.getPlayerDataByName(args[0]);
        if (data == null) {
            LOGGER.warning("§4Some errors occur in data processing.");
            return true;
        } else {
            data.qqNumber = Long.parseLong(args[1]);
            data.linkedTime = System.currentTimeMillis();
            data.qqChecked = true;
            Main.INSTANCE.dataManager.setPlayerDataByName(args[0], data);
            sender.sendMessage("§e已将游戏名为" + args[0] + "的玩家绑定QQ为" + args[1]);
        }
        return true;
    }
}
