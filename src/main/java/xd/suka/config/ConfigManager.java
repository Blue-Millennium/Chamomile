package xd.suka.config;

import org.jetbrains.annotations.NotNull;

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
    private static @NotNull Properties getProperties() {
        Properties properties = new Properties();

        properties.setProperty("QQCheckEnabled", String.valueOf(Config.qqCheckEnabled));
        properties.setProperty("SyncChatEnabled", String.valueOf(Config.syncChatEnabled));
        properties.setProperty("BotWsUrl", Config.botWsUrl);
        properties.setProperty("BotWsToken", Config.botWsToken);
        properties.setProperty("SyncChatGroup", String.valueOf(Config.syncChatGroup));
        properties.setProperty("JoinServerMessage", String.valueOf(Config.joinServerMessage));
        properties.setProperty("LeaveServerMessage", String.valueOf(Config.leaveServerMessage));
        properties.setProperty("SayServerMessage", String.valueOf(Config.sayServerMessage));
        properties.setProperty("SayQQMessage", String.valueOf(Config.sayQQMessage));
        properties.setProperty("DisTitle", String.valueOf(Config.disTitle));

        return properties;
    }

    public void load() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.load(reader);

            Config.qqCheckEnabled = Boolean.parseBoolean(properties.getProperty("QQCheckEnabled"));
            Config.syncChatEnabled = Boolean.parseBoolean(properties.getProperty("SyncChatEnabled"));
            Config.botWsUrl = properties.getProperty("BotWsUrl");
            Config.botWsToken = properties.getProperty("BotWsToken");
            Config.syncChatGroup = Long.parseLong(properties.getProperty("SyncChatGroup"));
            Config.joinServerMessage = properties.getProperty("JoinServerMessage");
            Config.leaveServerMessage = properties.getProperty("LeaveServerMessage");
            Config.sayServerMessage = properties.getProperty("SayServerMessage");
            Config.sayQQMessage = properties.getProperty("SayQQMessage");
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
            Properties properties = getProperties();

            properties.store(writer,null);
        } catch (Exception exception) {
            LOGGER.error("Failed to save config", exception);
        }
    }
}
