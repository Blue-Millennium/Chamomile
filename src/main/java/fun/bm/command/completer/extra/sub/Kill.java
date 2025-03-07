package fun.bm.command.completer.extra.sub;

import fun.bm.command.manager.model.CompleterE;
import fun.bm.util.OnlinePlayerListGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Kill extends CompleterE {
    public Kill() {
        super(null);
    }

    public List<String> tabCompleteMain(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            // 返回所有在线玩家的名字
            completions.addAll(OnlinePlayerListGet.GetOnlinePlayerList());
        }
        return completions;
    }
}
