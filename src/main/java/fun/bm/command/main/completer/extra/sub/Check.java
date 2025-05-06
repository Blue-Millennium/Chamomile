package fun.bm.command.main.completer.extra.sub;

import fun.bm.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Check extends Command.CompleterE {
    public Check() {
        super("check");
    }

    public List<String> CompleteMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
