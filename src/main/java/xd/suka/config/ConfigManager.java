package xd.suka.config;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

import static xd.suka.Main.CONFIG_FILE;
import static xd.suka.Main.LOGGER;

/**
 * @author Liycxc
 * Date: 2024/8/2 下午6:36
 */
public class ConfigManager {
    public void load() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.load(reader);

            Config.botWsUrl = properties.getProperty("BotWsUrl");
            Config.botWsToken = properties.getProperty("BotWsToken");
            Config.disTitle = properties.getProperty("DisTitle");
        } catch (Exception exception) {
            LOGGER.error("Failed to load config", exception);
            save();
        }
    }

    public void save() {
        if (!CONFIG_FILE.exists()) {
            try {
                if (!CONFIG_FILE.createNewFile()) {
                    LOGGER.error("Failed to create config file");
                }
            } catch (Exception exception) {
                LOGGER.error("Failed to create config file", exception);
            }
        }

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            Properties properties = new Properties();

            properties.setProperty("BotWsUrl", Config.botWsUrl);
            properties.setProperty("BotWsToken", Config.botWsToken);
            properties.setProperty("DisTitle", Config.disTitle);

            properties.store(writer,null);
        } catch (Exception exception) {
            LOGGER.error("Failed to save config", exception);
        }
    }
}
