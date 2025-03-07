package fun.bm.command.executor.vanilla;

import fun.bm.command.manager.model.ExecutorV;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.util.CommandOperatorCheck.checkNotOperator;

public class Kill extends ExecutorV {
    public Kill() {
        super("kill");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (vanilla) {
            vanillaCommand(sender, args);
        }
        if (checkNotOperator(sender)) {
            return true;
        }
        if (args.length == 0) {
            return vanillaCommand(sender, args);
        }
        if (args[0].startsWith("@e")) {
            if (args[0].equals("@e")) {
                Command.broadcastCommandMessage(sender, "§c拒绝执行清除全部实体，请指定具体实体类型");
                return true;
            } else if (String.join(" ", args).matches(".*?\\btype\\s*=\\s*!.*")) {
                Command.broadcastCommandMessage(sender, "§c拒绝执行清除多类型全部实体，请指定具体实体类型");
                return true;
            }
        }
        switch (args[0]) {
            case "@a" -> {
                Command.broadcastCommandMessage(sender, "§c拒绝执行清除全部玩家，请选择其他具体实体类型");
                return true;
            }
            case "@r" -> {
                Command.broadcastCommandMessage(sender, "§c拒绝执行清除随机玩家，请选择其他具体实体类型");
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
