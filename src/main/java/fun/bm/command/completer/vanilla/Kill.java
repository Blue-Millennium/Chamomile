package fun.bm.command.completer.vanilla;

import fun.bm.command.manager.model.CompleterV;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.command.completer.vanilla.Ban.getOnlinePlayerNames;

public class Kill extends CompleterV {
    public Kill() {
        super("kill");
    }

    public List<String> CompleteMain(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(getOnlinePlayerNames(sender, args[0]));
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
