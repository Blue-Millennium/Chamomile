package fun.bm.util.helper;

import org.bukkit.command.CommandSender;

public class OperatorChecker {
    public static boolean checkNotOperator(CommandSender sender) {
        boolean isNotOperator = !sender.isOp();
        if (isNotOperator) {
            sender.sendMessage("您没有权限这么做");
        }
        return isNotOperator;
    }
}
