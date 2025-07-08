package fun.bm.chamomile.command.modules.executor.extra.sub.check;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.config.modules.Bot.AuthConfig;
import fun.bm.chamomile.data.manager.data.Data;
import fun.bm.chamomile.util.MainEnv;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.chamomile.function.modules.data.QQCheck.generateCode;

public class Verify extends Command.ExecutorE {
    public Verify() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        Data data = MainEnv.dataManager.getPlayerDataByName(sender.getName());
        data = MainEnv.dataManager.nullCheck(data);
        int code = generateCode(data);
        sender.sendMessage(AuthConfig.disconnectMessage.replace("%CODE%", String.valueOf(code)));
        return true;
    }
}
