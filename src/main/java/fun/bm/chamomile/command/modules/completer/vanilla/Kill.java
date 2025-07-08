package fun.bm.chamomile.command.modules.completer.vanilla;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.util.helper.CommandHelper;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Kill extends Command.CompleterV {
    public Kill() {
        super("kill");
    }

    public List<String> completerMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, @NotNull String[] args) {
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
