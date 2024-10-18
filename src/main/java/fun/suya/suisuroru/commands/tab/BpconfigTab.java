package fun.suya.suisuroru.commands.tab;

import fun.suya.suisuroru.config.ConfigKeys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Suisuroru
 * Date: 2024/10/18 22:11
 * function: Provides tab completion for the bpconfig command
 */
public class BpconfigTab implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("reload");
            completions.add("query");
            completions.add("set");
        } else if (args.length == 2) {
            if (args[1].equals("query") || args[1].equals("set")) {
                completions.addAll(ConfigKeys.configKeysList.keySet());
            }
        }
        return List.of();
    }
}
