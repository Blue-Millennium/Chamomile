package fun.bm.chamomile.command.modules.executor.extra.sub;

import fun.bm.chamomile.command.ExtraCommand;
import fun.bm.chamomile.command.modules.executor.extra.sub.config.Query;
import fun.bm.chamomile.command.modules.executor.extra.sub.config.Reload;
import fun.bm.chamomile.command.modules.executor.extra.sub.config.Set;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

/**
 * @author Suisuroru
 * Date: 2024/10/14 22:41
 * function: Config settings
 */
public class Config extends ExtraCommand implements CommandExecutor {
    Reload reload = new Reload();
    Query query = new Query();
    Set set = new Set();

    public Config() {
        super("cmconfig");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (operatorCheck(sender)) {
            return true;
        }
        if (args.length == 0) {
            if (sender instanceof Player) {
                sender.sendMessage("§e检查到执行者为OP，已使用颜色区分需要管理员权限的指令，绿色为通用指令，红色为管理员权限指令");
                sender.sendMessage("§e在下方的指令中，您可以使用cm来代替输入chamomile");
                sender.sendMessage("§4重载配置文件:使用/chamomile config reload");
                sender.sendMessage("§4查询配置项:/chamomile config query <配置项>");
                sender.sendMessage("§4修改配置项:/chamomile config set <配置项> <修改值>");
            } else {
                sender.sendMessage("检查到执行者为控制台，已使用前缀区分玩家指令及管理员指令");
                sender.sendMessage("在下方的指令中，您可以使用cm来代替输入chamomile");
                sender.sendMessage("[管理员/控制台]重载配置文件:/chamomile config reload");
                sender.sendMessage("[管理员/控制台]查询配置项:/chamomile config query <配置项>");
                sender.sendMessage("[管理员/控制台]修改配置项:/chamomile config set <配置项> <修改值>");
            }
        } else {

            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            switch (args[0].toLowerCase()) {
                case "reload" -> reload.onCommand(sender, command, label, subArgs);
                case "query" -> query.onCommand(sender, command, label, subArgs);
                case "set" -> set.onCommand(sender, command, label, subArgs);
                default -> sender.sendMessage("Unknown command. Usage: /chamomile config [reload|query|set] [args...]");
            }
        }
        return true;
    }
}