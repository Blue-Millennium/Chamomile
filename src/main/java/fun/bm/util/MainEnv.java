package fun.bm.util;

import fun.bm.Chamomile;
import fun.bm.command.CommandManager;
import fun.bm.config.ConfigManager;
import fun.bm.data.PlayerData.DataManager;
import fun.bm.module.ModuleManager;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class MainEnv {
    public static Logger LOGGER = null;
    public static Chamomile INSTANCE = null;
    public static Bot BOT;
    public static List<File> OLD_BASE_DIR = List.of();
    public static File BASE_DIR = new File("plugins/Chamomile");
    public static File DATA_FILE = new File(BASE_DIR, "data.json");
    public static File CONFIG_FILE = new File(BASE_DIR, "config.properties");
    public static File REPORT_DATA_FILE = new File(BASE_DIR, "report.csv");
    public static File UNION_BAN_DATA_FILE = new File(BASE_DIR, "unionbandata.json");
    public static GlobalEventChannel eventChannel;
    public static DataManager dataManager;
    public static ConfigManager configManager;
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
}
