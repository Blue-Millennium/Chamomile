package fun.bm.command.main.executor.extra.sub.check;

import fun.bm.command.Command;
import fun.bm.config.Config;
import fun.bm.data.PlayerData.Data;
import fun.bm.util.MainEnv;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.module.impl.PlayerDataProcess.QQCheck.NullCheck;
import static fun.bm.module.impl.PlayerDataProcess.QQCheck.generateCode;

public class Verify extends Command.ExecutorE {
    public Verify() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        Data data = MainEnv.dataManager.getPlayerDataByName(sender.getName());
        data = NullCheck(data);
        int code = generateCode(data);
        sender.sendMessage(Config.DisTitle.replace("%CODE%", String.valueOf(code)));
        return true;
    }
}
