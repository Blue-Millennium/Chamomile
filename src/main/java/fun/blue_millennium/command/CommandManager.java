package fun.blue_millennium.command;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {
    public static void registerCommand(JavaPlugin plugin) {
        if (fun.blue_millennium.config.Config.VanillaCommandsRewritten) {
            // Vanilla commands
            plugin.getCommand("ban").setExecutor(new fun.blue_millennium.command.execute.vanilla.Ban());
            plugin.getCommand("ban").setTabCompleter(new fun.blue_millennium.command.tab.vanilla.Ban());

            plugin.getCommand("kill").setExecutor(new fun.blue_millennium.command.execute.vanilla.Kill());
            plugin.getCommand("kill").setTabCompleter(new fun.blue_millennium.command.tab.vanilla.Kill());

            plugin.getCommand("pardon").setExecutor(new fun.blue_millennium.command.execute.vanilla.Pardon());
            plugin.getCommand("pardon").setTabCompleter(new fun.blue_millennium.command.tab.vanilla.Pardon());
        }

        // New functions
        plugin.getCommand("chamomile").setExecutor(new fun.blue_millennium.command.execute.othercommands.Chamomile());
        plugin.getCommand("chamomile").setTabCompleter(new fun.blue_millennium.command.tab.othercommands.Chamomile());

        plugin.getCommand("chamomilehelp").setExecutor(new fun.blue_millennium.command.execute.othercommands.sub.Help());

        plugin.getCommand("cmconfig").setExecutor(new fun.blue_millennium.command.execute.othercommands.sub.Config());
        plugin.getCommand("cmconfig").setTabCompleter(new fun.blue_millennium.command.tab.othercommands.sub.Config());

        plugin.getCommand("cmdata").setExecutor(new fun.blue_millennium.command.execute.othercommands.sub.Data());
        plugin.getCommand("cmdata").setTabCompleter(new fun.blue_millennium.command.tab.othercommands.sub.Data());

        plugin.getCommand("cmreload").setExecutor(new fun.blue_millennium.command.execute.othercommands.sub.config.Reload());

        plugin.getCommand("link").setExecutor(new fun.blue_millennium.command.execute.othercommands.sub.data.Bind());
        plugin.getCommand("link").setTabCompleter(new fun.blue_millennium.command.tab.othercommands.sub.Link());

        plugin.getCommand("query-report").setExecutor(new fun.blue_millennium.command.execute.othercommands.sub.report.ReportQuery());

        plugin.getCommand("report").setExecutor(new fun.blue_millennium.command.execute.othercommands.sub.report.Report());
        plugin.getCommand("report").setTabCompleter(new fun.blue_millennium.command.tab.othercommands.sub.Report());

        plugin.getCommand("rcon").setExecutor(new fun.blue_millennium.command.execute.othercommands.sub.Rcon());
    }
}
