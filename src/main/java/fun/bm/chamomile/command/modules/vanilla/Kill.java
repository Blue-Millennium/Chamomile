package fun.bm.chamomile.command.modules.vanilla;

import fun.bm.chamomile.command.VanillaCommand;
import fun.bm.chamomile.util.helper.CommandHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

public class Kill extends VanillaCommand implements CommandExecutor, TabCompleter {
    public Kill() {
        super("kill");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (operatorCheck(sender)) {
            return true;
        }
        if (args.length == 0) {
            return vanillaExecutor(sender, args);
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
                return vanillaExecutor(sender, args);
            }
        }
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(CommandHelper.getOnlinePlayerList(args[0]));
            completions.add("@e");
            completions.add("@a");
            completions.add("@p");
            completions.add("@r");
            completions.add("items");
        } else if (args.length == 2) {
            if (args[0].matches("@[earp]")) {
                completions.add("sort=");
                completions.add("limit=");
                completions.add("x=");
                completions.add("y=");
                completions.add("z=");
                completions.add("dx=");
                completions.add("dy=");
                completions.add("dz=");
                completions.add("distance=");
                completions.add("level=");
                completions.add("gamemode=");
                completions.add("name=");
                completions.add("team=");
                completions.add("tag=");
                completions.add("type=");
                completions.add("nbt=");
                completions.add("predicate=");
                completions.add("advancements=");
                completions.add("scores=");
            }
        }

        return completions;
    }
}
