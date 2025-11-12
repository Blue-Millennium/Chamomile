package fun.bm.chamomile.command.modules.extra.sub.check;

import fun.bm.chamomile.command.ExtraCommand;
import fun.bm.chamomile.config.modules.Bot.AuthConfig;
import fun.bm.chamomile.data.basedata.BaseData;
import fun.bm.chamomile.util.Environment;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.chamomile.function.modules.data.QQCheck.generateCode;

public class Verify extends ExtraCommand implements CommandExecutor {
    public Verify() {
        super(null);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        BaseData data = Environment.dataManager.baseDataManager.getPlayerDataByName(sender.getName());
        data = Environment.dataManager.baseDataManager.nullCheck(data);
        int code = generateCode(data);
        sender.sendMessage(AuthConfig.disconnectMessage.replace("%CODE%", String.valueOf(code)));
        return true;
    }
}
