package fun.bm.chamomile.util;

import fun.bm.chamomile.Chamomile;
import fun.bm.chamomile.command.CommandManager;
import fun.bm.chamomile.config.ConfigManager;
import fun.bm.chamomile.data.manager.data.DataManager;
import fun.bm.chamomile.function.FunctionManager;
import fun.bm.chamomile.util.helper.EmailSender;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class MainEnv {
    public final static File BASE_DIR = new File("plugins/Chamomile");
    public final static File DATA_FILE = new File(BASE_DIR, "data.json");
    public final static File CONFIG_FILE = new File(BASE_DIR, "config.toml");
    public final static File REPORT_DATA_FILE = new File(BASE_DIR, "report.csv");
    public final static File UNION_BAN_DATA_FILE = new File(BASE_DIR, "unionbandata.json");
    public final static DataManager dataManager = new DataManager();
    public final static ConfigManager configManager = new ConfigManager();
    public final static FunctionManager functionManager = new FunctionManager();
    public final static CommandManager commandManager = new CommandManager();
    public final static EmailSender emailSender = new EmailSender();
    public static Logger LOGGER;
    public static Chamomile INSTANCE;
    public static Bot BOT;
    public static List<File> OLD_BASE_DIR;
    public static GlobalEventChannel eventChannel;
}
