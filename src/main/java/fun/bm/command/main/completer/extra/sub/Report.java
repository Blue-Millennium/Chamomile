package fun.bm.command.main.completer.extra.sub;

import fun.bm.command.Command;
import fun.bm.util.helper.CommandHelper;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Suisuroru
 * Date: 2024/10/15 01:41
 * function: Provides tab completion for the report command
 */
public class Report extends Command.CompleterE {

    public Report() {
        super("report");
    }

    public List<String> CompleteMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            // 返回所有在线玩家的名字
            completions.addAll(CommandHelper.GetOnlinePlayerList());
        }
        return completions;
    }
}
