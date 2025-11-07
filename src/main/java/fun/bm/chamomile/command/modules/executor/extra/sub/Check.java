package fun.bm.chamomile.command.modules.executor.extra.sub;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.command.modules.executor.extra.sub.check.Bind;
import fun.bm.chamomile.command.modules.executor.extra.sub.check.Delete;
import fun.bm.chamomile.command.modules.executor.extra.sub.check.OpDelete;
import fun.bm.chamomile.command.modules.executor.extra.sub.check.Verify;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Check extends Command.ExecutorE {
    Delete delete = new Delete();
    OpDelete opDelete = new OpDelete();
    Verify verify = new Verify();
    Bind bind = new Bind();

    public Check() {
        super("check");
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                if (command.getName().equals("check")) {
                    sender.sendMessage("§c删除已经绑定的数据: /check del");
                    sender.sendMessage("§c再次绑定数据: /check verify");
                    sender.sendMessage("§e删除已经绑定的数据: /check opdel <游戏ID> <时间戳>");
                    sender.sendMessage("§e绑定QQ数据:使用/check bind <游戏ID> qq <QQ号>");
                    sender.sendMessage("§e绑定Userid数据:使用/check bind <游戏ID> userid <Userid> <Userid-Group>");
                } else {
                    sender.sendMessage("在下方的指令中，您可以使用cm来代替输入chamomile");
                    sender.sendMessage("§c删除已经绑定的数据: /chamomile check del");
                    sender.sendMessage("§c再次绑定数据: /chamomile check verify");
                    sender.sendMessage("§e删除已经绑定的数据: /chamomile check opdel <游戏ID> <时间戳>");
                    sender.sendMessage("§e绑定QQ数据:使用/chamomile check bind <游戏ID> qq <QQ号>");
                    sender.sendMessage("§e绑定Userid数据:使用/chamomile check bind <游戏ID> userid <Userid> <Userid-Group>");
                }
            } else {
                if (command.getName().equals("check")) {
                    sender.sendMessage("检查到执行者为控制台，已使用前缀区分玩家指令及管理员指令");
                    sender.sendMessage("[玩家]删除已经绑定的数据: /check del");
                    sender.sendMessage("[玩家]再次绑定数据: /check verify");
                    sender.sendMessage("[管理员/控制台]删除已经绑定的数据: /check opdel <游戏ID> <时间戳>");
                    sender.sendMessage("[管理员/控制台]绑定QQ数据:使用/check bind <游戏ID> qq <QQ号>");
                    sender.sendMessage("[管理员/控制台]绑定Userid数据:使用/check bind <游戏ID> userid <Userid> <Userid-Group>");
                } else {
                    sender.sendMessage("检查到执行者为控制台，已使用前缀区分玩家指令及管理员指令");
                    sender.sendMessage("在下方的指令中，您可以使用cm来代替输入chamomile");
                    sender.sendMessage("[玩家]删除已经绑定的数据: /chamomile check del");
                    sender.sendMessage("[玩家]再次绑定数据: /chamomile check verify");
                    sender.sendMessage("[管理员/控制台]删除已经绑定的数据: /chamomile check opdel <游戏ID> <时间戳>");
                    sender.sendMessage("[管理员/控制台]绑定QQ数据:使用/chamomile check bind <游戏ID> qq <QQ号>");
                    sender.sendMessage("[管理员/控制台]绑定Userid数据:使用/chamomile check bind <游戏ID> userid <Userid> <Userid-Group>");
                }
            }
        } else {
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0].toLowerCase()) {
                case "del" -> delete.onCommand(sender, command, label, subArgs);
                case "opdel" -> opDelete.onCommand(sender, command, label, subArgs);
                case "verify" -> verify.onCommand(sender, command, label, subArgs);
                case "bind" -> bind.onCommand(sender, command, label, subArgs);
                default -> {
                    if (command.getName().equals("check")) {
                        sender.sendMessage("Unknown command. Usage: /check [del|verify] [args...]");
                    } else {
                        sender.sendMessage("Unknown command. Usage: /chamomile check [del|verify] [args...]");
                    }
                }
            }
        }
        return true;
    }
}
