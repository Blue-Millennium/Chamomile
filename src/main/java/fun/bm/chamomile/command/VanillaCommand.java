package fun.bm.chamomile.command;

import fun.bm.chamomile.config.modules.ServerConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public class VanillaCommand extends ExtraCommand {
    public boolean vanilla = false;

    public VanillaCommand(@Nullable String commandName) {
        super(commandName);
    }

    public boolean vanillaExecutor(CommandSender sender, String[] args) {
        return Bukkit.dispatchCommand(sender, "minecraft:" + commandName + " " + String.join(" ", args));
    }

    public void setupCommand() {
        if (!ServerConfig.vanillaCommandsRewritten) {
            vanilla = true;
        }
    }
}
