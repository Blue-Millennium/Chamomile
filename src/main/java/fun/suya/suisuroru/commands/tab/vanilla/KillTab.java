package fun.suya.suisuroru.commands.tab.vanilla;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static fun.suya.suisuroru.commands.tab.vanilla.BanTab.getOnlinePlayerNames;

public class KillTab implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(getOnlinePlayerNames(sender,args[0]));
            completions.add("@e");
            completions.add("@a");
            completions.add("@p");
            completions.add("@r");
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
