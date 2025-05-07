package fun.bm.command.main.executor.extra.sub.data;

import fun.bm.command.Command;
import fun.bm.data.LoginData.Data;
import fun.bm.util.MainEnv;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.util.MainEnv.LOGGER;
import static fun.bm.util.helper.CommandHelper.checkNotOperator;

public class Bind extends Command.ExecutorE {
    public Bind() {
        super("bind");
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (checkNotOperator(sender)) {
            return true;
        }
        Data data = MainEnv.dataManager.getPlayerDataByName(args[0]);
        if (data == null) {
            LOGGER.warning("§4Some errors occur in data processing.");
            return true;
        } else {
            data.qqNumber = Long.parseLong(args[1]);
            data.linkedTime = System.currentTimeMillis();
            data.qqChecked = true;
            MainEnv.dataManager.setPlayerDataByName(args[0], data);
            sender.sendMessage("§e已将游戏名为" + args[0] + "的玩家绑定QQ为" + args[1]);
        }
        return true;
    }
}
