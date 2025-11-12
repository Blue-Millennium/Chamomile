package fun.bm.chamomile.command.modules.completer.extra.sub;

import fun.bm.chamomile.command.ExtraCommand;
import fun.bm.chamomile.util.Environment;
import fun.bm.chamomile.util.helper.CommandHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Suisuroru
 * Date: 2024/10/15 01:41
 * function: Provides tab completion for the report command
 */
public class Report extends ExtraCommand implements TabCompleter {
    public Report() {
        super("report");
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(CommandHelper.getOnlinePlayerList(args[0]));
        } else if (args.length == 2) {
            Environment.dataManager.reportDataManager.readReportFile().forEach(row -> {
                for (int i = 1; i < row.size(); i++) {
                    completions.add(row.get(i));
                }
            });
        }
        return completions;
    }
}
