package fun.bm.chamomile.util;

import fun.bm.chamomile.Chamomile;
import fun.bm.chamomile.command.CommandManager;
import fun.bm.chamomile.config.ConfigManager;
import fun.bm.chamomile.data.DataManager;
import fun.bm.chamomile.function.FunctionManager;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;

import java.io.File;
import java.util.logging.Logger;

public class Environment {
    public final static File BASE_DIR = new File("plugins/Chamomile");
    public final static DataManager dataManager = new DataManager();
    public final static ConfigManager configManager = new ConfigManager();
    public final static FunctionManager functionManager = new FunctionManager();
    public final static CommandManager commandManager = new CommandManager();
    public static Logger LOGGER;
    public static Chamomile INSTANCE;
    public static Bot BOT;
    public static GlobalEventChannel eventChannel;
}
