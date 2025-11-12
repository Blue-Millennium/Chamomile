package fun.bm.chamomile.command.modules.completer.vanilla;

import fun.bm.chamomile.command.VanillaCommand;
import fun.bm.chamomile.util.helper.CommandHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Ban extends VanillaCommand implements TabCompleter {
    public Ban() {
        super("ban");
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return CommandHelper.getOnlinePlayerList(args[0]);
        }
        return new ArrayList<>();
    }
}
