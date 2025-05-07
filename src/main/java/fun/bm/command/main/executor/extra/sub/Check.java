package fun.bm.command.main.executor.extra.sub;

import fun.bm.command.Command;
import fun.bm.command.main.executor.extra.sub.check.Delete;
import fun.bm.command.main.executor.extra.sub.check.Verify;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Check extends Command.ExecutorE {
    Delete delete = new Delete();
    Verify verify = new Verify();

    public Check() {
        super("check");
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (command.getName().equals("check")) {
                sender.sendMessage("§c删除已经绑定的数据: /check del");
                sender.sendMessage("§c再次绑定数据: /check verify");
            } else {
                sender.sendMessage("在下方的指令中，您可以使用cm来代替输入chamomile");
                sender.sendMessage("§c删除已经绑定的数据: /chamomile check del");
                sender.sendMessage("§c再次绑定数据: /chamomile check verify");
            }
        } else {
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0].toLowerCase()) {
                case "del": {
                    delete.onCommand(sender, command, label, subArgs);
                    break;
                }
                case "verify": {
                    verify.onCommand(sender, command, label, subArgs);
                    break;
                }
                default: {
                    if (command.getName().equals("check")) {
                        sender.sendMessage("Unknown command. Usage: /check [del|verify] [args...]");
                    } else {
                        sender.sendMessage("Unknown command. Usage: /chamomile check [del|verify] [args...]");
                    }
                    break;
                }
            }
        }
        return true;
    }
}
