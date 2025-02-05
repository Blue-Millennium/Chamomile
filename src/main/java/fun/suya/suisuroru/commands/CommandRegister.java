package fun.suya.suisuroru.commands;

import fun.suya.suisuroru.commands.execute.othercommands.sub.*;
import fun.suya.suisuroru.commands.execute.othercommands.sub.config.Reload;
import fun.suya.suisuroru.commands.tab.othercommands.BasePlugin;
import fun.suya.suisuroru.commands.tab.vanilla.Ban;
import fun.suya.suisuroru.commands.tab.vanilla.Kill;
import fun.suya.suisuroru.commands.tab.vanilla.Pardon;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegister {
    public static void registerCommand(JavaPlugin plugin) {
        if (fun.suya.suisuroru.config.Config.VanillaCommandsRewritten) {
            // vanilla functions
            plugin.getCommand("ban").setExecutor(new fun.suya.suisuroru.commands.execute.vanilla.Ban());
            plugin.getCommand("ban").setTabCompleter(new Ban());
            plugin.getCommand("pardon").setExecutor(new fun.suya.suisuroru.commands.execute.vanilla.Pardon());
            plugin.getCommand("pardon").setTabCompleter(new Pardon());
            plugin.getCommand("kill").setExecutor(new fun.suya.suisuroru.commands.execute.vanilla.Kill());
            plugin.getCommand("kill").setTabCompleter(new Kill());
        }
        // new functions
        plugin.getCommand("basepluginhelp").setExecutor(new Help());
        plugin.getCommand("report").setExecutor(new Report());
        plugin.getCommand("report").setTabCompleter(new fun.suya.suisuroru.commands.tab.othercommands.sub.Report());
        plugin.getCommand("bpconfig").setExecutor(new Config());
        plugin.getCommand("bpconfig").setTabCompleter(new fun.suya.suisuroru.commands.tab.othercommands.sub.Config());
        plugin.getCommand("bpreload").setExecutor(new Reload());
        plugin.getCommand("baseplugin").setExecutor(new fun.suya.suisuroru.commands.execute.othercommands.BasePlugin());
        plugin.getCommand("baseplugin").setTabCompleter(new BasePlugin());
        plugin.getCommand("query-report").setExecutor(new ReportQuery());
        plugin.getCommand("bpdata").setExecutor(new Data());
        plugin.getCommand("bpdata").setTabCompleter(new fun.suya.suisuroru.commands.tab.othercommands.sub.Data());
        plugin.getCommand("rcon").setExecutor(new Rcon());
    }
}
