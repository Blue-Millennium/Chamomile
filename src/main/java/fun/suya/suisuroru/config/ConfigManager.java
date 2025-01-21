package fun.suya.suisuroru.config;

import fun.xd.suka.Main;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static fun.xd.suka.Main.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/10/14 23:41
 * function: Manage config, rewrote by Suisuroru in 250121
 */
public class ConfigManager {

    public static List<String> getConfigFieldNames() {
        Field[] fields = Config.class.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }

    private static @NotNull Properties getDefaultProperties() {
        Properties properties = new Properties();
        List<String> fieldNames = getConfigFieldNames();

        for (String fieldName : fieldNames) {
            try {
                Field field = Config.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                properties.setProperty(fieldName, field.get(null).toString());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.warning("Failed to get default config value for " + fieldName + ": " + e.getMessage());
            }
        }

        return properties;
    }

    private static @NotNull Properties getProperties() {
        Properties properties = new Properties();
        List<String> fieldNames = getConfigFieldNames();
        for (String fieldName : fieldNames) {
            try {
                Field field = Config.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                properties.setProperty(fieldName, field.get(null).toString());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.warning("Failed to get config value for " + fieldName + ": " + e.getMessage());
            }
        }

        return properties;
    }

    public void load() {
        try (FileReader reader = new FileReader(Main.CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.load(reader);
            LOGGER.info("Loaded config file: " + Main.CONFIG_FILE.getAbsolutePath());

            Properties defaultProperties = getDefaultProperties();
            List<String> fieldNames = getConfigFieldNames();
            boolean label = false;

            for (String fieldName : fieldNames) {
                try {
                    Field field = Config.class.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    String value = properties.getProperty(fieldName);
                    if (value == null) {
                        value = defaultProperties.getProperty(fieldName);
                        label = true;
                    }
                    Object parsedValue = parseValue(field.getType(), value);
                    field.set(null, convertToFieldType(field.getType(), parsedValue));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    LOGGER.warning("Failed to set config value for " + fieldName + ": " + e.getMessage());
                }
            }
            if (label) {
                save();
            }
        } catch (IOException e) {
            LOGGER.warning("Failed to load config file " + Main.CONFIG_FILE.getAbsolutePath() + ": " + e.getMessage());
            LOGGER.info("Using default config values.");
            save();
        }
    }

    private Object convertToFieldType(Class<?> fieldType, Object value) {
        if (fieldType == boolean.class || fieldType == Boolean.class) {
            return Boolean.parseBoolean(value.toString());
        } else if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(value.toString());
        } else if (fieldType == long.class || fieldType == Long.class) {
            return Long.parseLong(value.toString());
        } else {
            return value;
        }
    }

    public void setConfigValue(String fieldName, String value) {
        try {
            Field field = Config.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object parsedValue = parseValue(field.getType(), value);
            field.set(null, parsedValue);
            save();
        } catch (Exception e) {
            LOGGER.warning("Failed to parse config value for " + fieldName + ": " + e.getMessage());
        }
    }

    private Object parseValue(Class<?> type, String value) {
        if (type == Boolean.class) return Boolean.parseBoolean(value);
        else if (type == Integer.class) return Integer.parseInt(value);
        else if (type == Long.class) return Long.parseLong(value);
        else return value;
    }

    public void save() {
        if (!Main.CONFIG_FILE.exists()) {
            try {
                if (!Main.CONFIG_FILE.createNewFile()) {
                    LOGGER.warning("Failed to create config file");
                }
            } catch (IOException e) {
                LOGGER.warning("Failed to create config file " + e.getMessage());
            }
        }

        try (FileWriter writer = new FileWriter(Main.CONFIG_FILE)) {
            Properties properties = getProperties();
            properties.store(writer, null);
            LOGGER.info("Saved config file: " + Main.CONFIG_FILE.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.warning("Failed to save config " + e.getMessage());
        }
    }
}
