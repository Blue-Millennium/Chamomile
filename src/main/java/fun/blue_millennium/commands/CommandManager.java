package fun.blue_millennium.commands;

import fun.blue_millennium.commands.execute.othercommands.BasePlugin;
import fun.blue_millennium.commands.execute.othercommands.sub.Help;
import fun.blue_millennium.commands.execute.othercommands.sub.Rcon;
import fun.blue_millennium.commands.execute.othercommands.sub.ReportQuery;
import fun.blue_millennium.commands.execute.othercommands.sub.config.Reload;
import fun.blue_millennium.commands.execute.othercommands.sub.data.Bind;
import fun.blue_millennium.commands.execute.vanilla.Ban;
import fun.blue_millennium.commands.execute.vanilla.Kill;
import fun.blue_millennium.commands.execute.vanilla.Pardon;
import fun.blue_millennium.commands.tab.othercommands.sub.Config;
import fun.blue_millennium.commands.tab.othercommands.sub.Data;
import fun.blue_millennium.commands.tab.othercommands.sub.Link;
import fun.blue_millennium.commands.tab.othercommands.sub.Report;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {
    public static void registerCommand(JavaPlugin plugin) {
        if (fun.blue_millennium.config.Config.VanillaCommandsRewritten) {
            // vanilla functions
            plugin.getCommand("ban").setExecutor(new Ban());
            plugin.getCommand("ban").setTabCompleter(new fun.blue_millennium.commands.tab.vanilla.Ban());
            plugin.getCommand("pardon").setExecutor(new Pardon());
            plugin.getCommand("pardon").setTabCompleter(new fun.blue_millennium.commands.tab.vanilla.Pardon());
            plugin.getCommand("kill").setExecutor(new Kill());
            plugin.getCommand("kill").setTabCompleter(new fun.blue_millennium.commands.tab.vanilla.Kill());
            plugin.getCommand("link").setTabCompleter(new Link());
        }
        // new functions
        plugin.getCommand("basepluginhelp").setExecutor(new Help());
        plugin.getCommand("report").setExecutor(new fun.blue_millennium.commands.execute.othercommands.sub.Report());
        plugin.getCommand("report").setTabCompleter(new Report());
        plugin.getCommand("bpconfig").setExecutor(new fun.blue_millennium.commands.execute.othercommands.sub.Config());
        plugin.getCommand("bpconfig").setTabCompleter(new Config());
        plugin.getCommand("bpreload").setExecutor(new Reload());
        plugin.getCommand("baseplugin").setExecutor(new BasePlugin());
        plugin.getCommand("baseplugin").setTabCompleter(new fun.blue_millennium.commands.tab.othercommands.BasePlugin());
        plugin.getCommand("query-report").setExecutor(new ReportQuery());
        plugin.getCommand("bpdata").setExecutor(new fun.blue_millennium.commands.execute.othercommands.sub.Data());
        plugin.getCommand("bpdata").setTabCompleter(new Data());
        plugin.getCommand("rcon").setExecutor(new Rcon());
        plugin.getCommand("link").setExecutor(new Bind());
    }
}
