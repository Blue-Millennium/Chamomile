package fun.bm.util;

import org.bukkit.command.CommandSender;

public class CommandOperatorCheck {
    public static boolean checkNotOperator(CommandSender sender) {
        boolean isNotOperator = !sender.isOp();
        if (isNotOperator) {
            sender.sendMessage("您没有权限这么做");
        }
        return isNotOperator;
    }
}
