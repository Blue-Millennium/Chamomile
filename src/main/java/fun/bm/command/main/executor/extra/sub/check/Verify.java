package fun.bm.command.main.executor.extra.sub.check;

import fun.bm.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Verify extends Command.CompleterE {
    public Verify() {
        super(null);
    }

    public List<String> CompleteMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
