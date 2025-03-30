package fun.bm.command.main.executor.vanilla;

import fun.bm.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.util.helper.CommandHelper.checkNotOperator;

public class Kill extends Command.ExecutorV {
    public Kill() {
        super("kill");
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (checkNotOperator(sender)) {
            return true;
        }
        if (args.length == 0) {
            return vanillaCommand(sender, args);
        }
        if (args[0].startsWith("@e")) {
            if (args[0].equals("@e")) {
                org.bukkit.command.Command.broadcastCommandMessage(sender, "§c拒绝执行清除全部实体，请指定具体实体类型");
                return true;
            } else if (String.join(" ", args).matches(".*?\\btype\\s*=\\s*!.*")) {
                org.bukkit.command.Command.broadcastCommandMessage(sender, "§c拒绝执行清除多类型全部实体，请指定具体实体类型");
                return true;
            }
        }
        switch (args[0]) {
            case "@a" -> {
                org.bukkit.command.Command.broadcastCommandMessage(sender, "§c拒绝执行清除全部玩家，请选择其他具体实体类型");
                return true;
            }
            case "@r" -> {
                org.bukkit.command.Command.broadcastCommandMessage(sender, "§c拒绝执行清除随机玩家，请选择其他具体实体类型");
                return true;
            }
            case "items" -> {
                return Bukkit.dispatchCommand(sender, "minecraft:kill @e[type=item]");
            }
            default -> {
                return vanillaCommand(sender, args);
            }
        }
    }
}
