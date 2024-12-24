package fun.suya.suisuroru.config;

import fun.xd.suka.Main;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

import static fun.xd.suka.Main.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/10/14 23:41
 * function: Manage config, rewrited by Suisuroru in 241014
 * additon: Config:代码层名字; ConfigKeys:配置项名字;
 * 由于遇到一些问题，两个配置文件暂时无法合并
 */
public class ConfigManager {
    private static final Map<String, String> configMapping = ConfigRemap.configMapping;

    private static @NotNull Properties getDefaultProperties() {
        Properties properties = new Properties();

        for (ConfigKeys key : ConfigKeys.values()) {
            properties.setProperty(key.name(), key.getDefaultValue().toString());
        }

        return properties;
    }

    private static @NotNull Properties getProperties() {
        Properties properties = new Properties();

        // 遍历 Config 类中的所有字段，并将它们的值保存到 Properties 对象中
        for (Map.Entry<String, String> entry : configMapping.entrySet()) {
            String fieldName = null;
            try {
                String key = entry.getKey();
                fieldName = entry.getValue();
                Object value = getValueFromConfig(fieldName);
                properties.setProperty(key, value.toString());
            } catch (Exception e) {
                LOGGER.warning("Failed to get config value for " + fieldName + ": " + e.getMessage());
            }
        }

        return properties;
    }

    private static Object getValueFromConfig(String fieldName) {
        try {
            Field field = Config.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get config value for " + fieldName, e);
        }
    }

    public void load() {
        try (FileReader reader = new FileReader(Main.INSTANCE.CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.load(reader);
            Properties defaultProperties = getDefaultProperties();

            for (ConfigKeys key : ConfigKeys.values()) {
                // 尝试从配置文件中获取值，如果没有找到，则使用默认值
                String value = properties.getProperty(key.name(), defaultProperties.getProperty(key.name()));
                setConfigValue(key, value);
            }

            save();

        } catch (Exception exception) {
            LOGGER.warning("Failed to load config: " + exception.getMessage());
            save();
        }
    }

    public void setConfigValue(ConfigKeys key, String value) {
        try {
            Object parsedValue = parseValue(key.getType(), value);
            setConfigField(key, parsedValue);
        } catch (Exception e) {
            LOGGER.warning("Failed to parse config value for " + key.name() + ": " + e.getMessage());
        }
    }

    private Object parseValue(Class<?> type, String value) {
        if (type == Boolean.class) return Boolean.parseBoolean(value);
        else if (type == Integer.class) return Integer.parseInt(value);
        else if (type == Long.class) return Long.parseLong(value);
        else return value;
    }

    private void setConfigField(ConfigKeys key, Object value) {
        switch (key) {
            case QQCheckEnabled -> Config.qqCheckEnabled = (Boolean) value;
            case SyncChatEnabled -> Config.syncChatEnabled = (Boolean) value;
            case BotWsUrl -> Config.botWsUrl = (String) value;
            case BotWsToken -> Config.botWsToken = (String) value;
            case SyncChatGroup -> Config.syncChatGroup = (Long) value;
            case ReportGroup -> Config.reportGroup = (Long) value;
            case JoinServerMessage -> Config.joinServerMessage = (String) value;
            case LeaveServerMessage -> Config.leaveServerMessage = (String) value;
            case SayServerMessage -> Config.sayServerMessage = (String) value;
            case SayQQMessage -> Config.sayQQMessage = (String) value;
            case ReportMessage -> Config.reportMessage = (String) value;
            case DisTitle -> Config.disTitle = (String) value;
            case WebhookUrl -> Config.webhookUrl = (String) value;
            case ServerName -> Config.servername = (String) value;
            case RconEnabled -> Config.RconEnabled = (Boolean) value;
            case ExecuteCommandPrefix -> Config.ExecuteCommandPrefix = (String) value;
            case RconEnabledGroups -> Config.RconEnabledGroups = (String) value;
            case RconIP -> Config.RconIP = (String) value;
            case RconPort -> Config.RconPort = (Integer) value;
            case RconPassword -> Config.RconPassword = (String) value;
            case RconEnforceOperator -> Config.RconEnforceOperator = (Boolean) value;
            case QQRobotEnabled -> Config.QQRobotEnabled = (Boolean) value;
            case UnionBanEnabled -> Config.UnionBanEnabled = (Boolean) value;
            case UnionBanCheckOnly -> Config.UnionBanCheckOnly = (Boolean) value;
            case UnionBanCheckUrl -> Config.UnionBanCheckUrl = (String) value;
            case UnionBanReportUrl -> Config.UnionBanReportUrl = (String) value;
            case UnionBanReportKey -> Config.UnionBanReportKey = (String) value;
        }

    }

    public void save() {
        if (!Main.INSTANCE.CONFIG_FILE.exists()) {
            try {
                if (!Main.INSTANCE.CONFIG_FILE.createNewFile()) {
                    LOGGER.warning("Failed to create config file");
                }
            } catch (IOException e) {
                LOGGER.warning("Failed to create config file " + e.getMessage());
            }
        }

        try (FileWriter writer = new FileWriter(Main.INSTANCE.CONFIG_FILE)) {
            Properties properties = getProperties();
            properties.store(writer, null);
        } catch (IOException e) {
            LOGGER.warning("Failed to save config " + e.getMessage());
        }
    }
}
