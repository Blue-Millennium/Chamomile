package fun.bm.chamomile.command.modules.executor.extra.sub.check;

import fun.bm.chamomile.command.ExtraCommand;
import fun.bm.chamomile.command.modules.executor.extra.sub.check.bind.BindQQ;
import fun.bm.chamomile.command.modules.executor.extra.sub.check.bind.BindUserid;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

public class Bind extends ExtraCommand implements CommandExecutor {
    BindQQ bindQQ = new BindQQ();
    BindUserid bindUserid = new BindUserid();

    public Bind() {
        super("bind");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (operatorCheck(sender)) {
            return true;
        }

        switch (args[1].toLowerCase()) {
            case "qq" -> bindQQ.onCommand(sender, command, label, args);
            case "userid" -> bindUserid.onCommand(sender, command, label, args);
            default -> sender.sendMessage("Unknown command. Usage: /chamomile check bind [qq|userid] [args]");
        }
        return true;
    }
}
