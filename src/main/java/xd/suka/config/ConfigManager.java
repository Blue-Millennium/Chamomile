package xd.suka.config;

import org.jetbrains.annotations.NotNull;
import xd.suka.Main;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

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
        properties.setProperty("ReportGroup", String.valueOf(Config.reportGroup));
        properties.setProperty("JoinServerMessage", String.valueOf(Config.joinServerMessage));
        properties.setProperty("LeaveServerMessage", String.valueOf(Config.leaveServerMessage));
        properties.setProperty("SayServerMessage", String.valueOf(Config.sayServerMessage));
        properties.setProperty("SayQQMessage", String.valueOf(Config.sayQQMessage));
        properties.setProperty("DisTitle", String.valueOf(Config.disTitle));
        properties.setProperty("ReportMessage", Config.reportMessage);
        properties.setProperty("WebhookUrl", Config.webhookUrl);
        properties.setProperty("ServerName", Config.servername);
        properties.setProperty("RconEnabled", String.valueOf(Config.RconEnabled));
        properties.setProperty("ExecuteCommandPrefix", Config.ExecuteCommandPrefix);
        properties.setProperty("RconEnabledGroups", Config.RconEnabledGroups);
        properties.setProperty("RconIP", Config.RconIP);
        properties.setProperty("RconPort", String.valueOf(Config.RconPort));
        properties.setProperty("RconPassword", Config.RconPassword);
        properties.setProperty("RconEnforceOperator", String.valueOf(Config.RconEnforceOperator));

        return properties;
    }

    public void load() {
        try (FileReader reader = new FileReader(Main.INSTANCE.CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.load(reader);

            // 设置默认值
            setDefaultIfMissing(properties, "QQCheckEnabled", "true");
            setDefaultIfMissing(properties, "SyncChatEnabled", "true");
            setDefaultIfMissing(properties, "BotWsUrl", "ws://0.0.0.0:3001");
            setDefaultIfMissing(properties, "BotWsToken", "114514");
            setDefaultIfMissing(properties, "SyncChatGroup", "721823314");
            setDefaultIfMissing(properties, "ReportGroup", "721823314");
            setDefaultIfMissing(properties, "JoinServerMessage", "%NAME% joined the server");
            setDefaultIfMissing(properties, "LeaveServerMessage", "%NAME% left the server");
            setDefaultIfMissing(properties, "SayServerMessage", "%NAME%: %MESSAGE%");
            setDefaultIfMissing(properties, "SayQQMessage", "[QQ] %NAME%: %MESSAGE%");
            setDefaultIfMissing(properties, "DisTitle", "%NAME% was logging in \nIP: %IP% %IPINFO% \nLoginResult: %RESULT%");
            setDefaultIfMissing(properties, "ReportMessage", "[QQLogin] 请完成登录验证, 验证码: %CODE%");
            setDefaultIfMissing(properties, "WebhookUrl", "http://localhost:6888/webhook");
            setDefaultIfMissing(properties, "ServerName", "ServerName");
            setDefaultIfMissing(properties, "RconEnabled", "true");
            setDefaultIfMissing(properties, "ExecuteCommandPrefix", "*#");
            setDefaultIfMissing(properties, "RconEnabledGroups", "721823314");
            setDefaultIfMissing(properties, "RconIP", "0.0.0.0");
            setDefaultIfMissing(properties, "RconPort", "25575");
            setDefaultIfMissing(properties, "RconPassword", "password");
            setDefaultIfMissing(properties, "RconEnforceOperator", "true");

            // 读取配置
            Config.qqCheckEnabled = Boolean.parseBoolean(properties.getProperty("QQCheckEnabled"));
            Config.syncChatEnabled = Boolean.parseBoolean(properties.getProperty("SyncChatEnabled"));
            Config.botWsUrl = properties.getProperty("BotWsUrl");
            Config.botWsToken = properties.getProperty("BotWsToken");
            Config.syncChatGroup = Long.parseLong(properties.getProperty("SyncChatGroup"));
            Config.reportGroup = Long.parseLong(properties.getProperty("ReportGroup"));
            Config.joinServerMessage = properties.getProperty("JoinServerMessage");
            Config.leaveServerMessage = properties.getProperty("LeaveServerMessage");
            Config.sayServerMessage = properties.getProperty("SayServerMessage");
            Config.sayQQMessage = properties.getProperty("SayQQMessage");
            Config.disTitle = properties.getProperty("DisTitle");
            Config.reportMessage = properties.getProperty("ReportMessage");
            Config.webhookUrl = properties.getProperty("WebhookUrl");
            Config.servername = properties.getProperty("ServerName");
            Config.RconEnabled = Boolean.parseBoolean(properties.getProperty("RconEnabled"));
            Config.ExecuteCommandPrefix = properties.getProperty("ExecuteCommandPrefix");
            Config.RconEnabledGroups = properties.getProperty("RconEnabledGroups");
            Config.RconIP = properties.getProperty("RconIP");
            Config.RconPort = Integer.parseInt(properties.getProperty("RconPort"));
            Config.RconPassword = properties.getProperty("RconPassword");
            Config.RconEnforceOperator = Boolean.parseBoolean(properties.getProperty("RconEnforceOperator"));
            save();

        } catch (Exception exception) {
            LOGGER.warning("Failed to load config " + exception.getMessage());
            save();
        }
    }

    private void setDefaultIfMissing(Properties properties, String key, String defaultValue) {
        if (!properties.containsKey(key)) {
            properties.setProperty(key, defaultValue);
        }
    }

    public void save() {
        if (!Main.INSTANCE.CONFIG_FILE.exists()) {
            try {
                if (!Main.INSTANCE.CONFIG_FILE.createNewFile()) {
                    LOGGER.warning("Failed to create config file");
                }
            } catch (Exception exception) {
                LOGGER.warning("Failed to create config file " + exception.getMessage());
            }
        }

        try (FileWriter writer = new FileWriter(Main.INSTANCE.CONFIG_FILE)) {
            Properties properties = getProperties();
            properties.store(writer, null);
        } catch (Exception exception) {
            LOGGER.warning("Failed to save config " + exception.getMessage());
        }
    }
}
