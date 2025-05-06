package fun.bm.command.main.executor.extra.sub;

import fun.bm.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Check extends Command.ExecutorE {
    public Check() {
        super("check");
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        return true;
    }
}
