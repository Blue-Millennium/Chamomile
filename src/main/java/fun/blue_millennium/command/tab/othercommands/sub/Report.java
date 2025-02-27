package fun.blue_millennium.command.tab.othercommands.sub;

import fun.blue_millennium.util.OnlinePlayerListGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Suisuroru
 * Date: 2024/10/15 01:41
 * function: Provides tab completion for the report command
 */
public class Report implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            // 返回所有在线玩家的名字
            completions.addAll(OnlinePlayerListGet.GetOnlinePlayerList());
        }
        return completions;
    }
}
