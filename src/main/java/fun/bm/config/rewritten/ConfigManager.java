package fun.bm.config.rewritten;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import fun.bm.util.MainEnv;
import fun.bm.util.helper.ClassLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static fun.bm.util.MainEnv.LOGGER;

public class ConfigManager {
    private static final Set<ConfigModule> ConfigModules = new HashSet<>();
    private static CommentedFileConfig configFileInstance;

    public static void load() {
        try {
            MainEnv.BASE_DIR.mkdir();
            if (!MainEnv.CONFIG_FILE.exists()) MainEnv.CONFIG_FILE.createNewFile();
        } catch (Exception e) {
            LOGGER.warning("Failed to create config file");
        }
        configFileInstance = CommentedFileConfig.of(MainEnv.CONFIG_FILE);
        configFileInstance.load();

        loadConfigModule();
    }

    public static void reload() {
        configFileInstance.clear();
        load();
    }

    private static void loadConfigModule() {
        ConfigModules.addAll(ClassLoader.loadClasses("fun/bm/config/modules", ConfigModule.class));

        for (ConfigModule module : ConfigModules) {
            Field[] fields = module.getClass().getDeclaredFields();

            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
                    boolean skipLoad = field.getAnnotation(DoNotLoad.class) != null;
                    ConfigInfo configInfo = field.getAnnotation(ConfigInfo.class);

                    if (skipLoad || configInfo == null) {
                        continue;
                    }

                    final String fullConfigKeyName = (module.category().length == 0 ? "" : String.join(".", module.category()) + ".") + module.name() + "." + configInfo.name();

                    field.setAccessible(true);
                    final Object currentValue;
                    try {
                        currentValue = field.get(null);
                    } catch (IllegalAccessException e) {
                        LOGGER.warning("Failed to get config value for " + field.getName() + ": " + e.getMessage());
                        continue;
                    }
                    boolean removed = fullConfigKeyName.equals("removed_config.removed");

                    if (!configFileInstance.contains(fullConfigKeyName) || removed) {
                        for (TransformedConfig transformedConfig : field.getAnnotationsByType(TransformedConfig.class)) {
                            final String oldConfigKeyName = String.join(".", transformedConfig.category()) + "." + transformedConfig.name();
                            Object oldValue = configFileInstance.get(oldConfigKeyName);
                            if (oldValue != null) {
                                boolean success = true;
                                if (transformedConfig.transform() && !removed) {
                                    try {
                                        for (Class<? extends DefaultTransformLogic> logic : transformedConfig.transformLogic()) {
                                            oldValue = logic.getDeclaredConstructor().newInstance().transform(oldValue);
                                        }
                                        configFileInstance.add(fullConfigKeyName, oldValue);
                                    } catch (Exception e) {
                                        success = false;
                                        LOGGER.warning("Failed to transform removed config " + transformedConfig.name() + "!");
                                    }
                                }

                                if (success) removeConfig(oldConfigKeyName, transformedConfig.category());
                                final String comments = configInfo.comment();

                                if (!comments.isBlank()) configFileInstance.setComment(fullConfigKeyName, comments);

                                if (!removed && configFileInstance.get(fullConfigKeyName) != null) break;
                            }
                        }
                        if (configFileInstance.get(fullConfigKeyName) != null) continue;
                        if (currentValue == null) {
                            LOGGER.warning("Config " + module.name() + "tried to add an null default value!");
                            continue;
                        }

                        final String comments = configInfo.comment();

                        if (!comments.isBlank()) {
                            configFileInstance.setComment(fullConfigKeyName, comments);
                        }

                        configFileInstance.add(fullConfigKeyName, currentValue);
                        continue;
                    }

                    final Object actuallyValue = configFileInstance.get(fullConfigKeyName);
                    try {
                        field.set(null, actuallyValue);
                    } catch (IllegalAccessException e) {
                        LOGGER.warning("Failed to set config value for " + field.getName() + ": " + e.getMessage());
                    }
                }
            }
        }

        configFileInstance.save();
    }

    private static void removeConfig(String name, String[] keys) {
        configFileInstance.remove(name);
        Object configAtPath = configFileInstance.get(String.join(".", keys));
        if (configAtPath instanceof UnmodifiableConfig && ((UnmodifiableConfig) configAtPath).isEmpty()) {
            removeConfig(keys);
        }
    }

    private static void removeConfig(String[] keys) {
        configFileInstance.remove(String.join(".", keys));
        Object configAtPath = configFileInstance.get(String.join(".", Arrays.copyOfRange(keys, 1, keys.length)));
        if (configAtPath instanceof UnmodifiableConfig && ((UnmodifiableConfig) configAtPath).isEmpty()) {
            removeConfig(Arrays.copyOfRange(keys, 1, keys.length));
        }
    }

    public static boolean setConfigAndSave(String[] keys, Object value) {
        if (setConfig(keys, value)) {
            saveConfig();
            return true;
        }
        return false;
    }

    public static boolean setConfigAndSave(String keys, Object value) {
        if (setConfig(keys, value)) {
            saveConfig();
            return true;
        }
        return false;
    }

    public static boolean setConfig(String[] keys, Object value) {
        if (configFileInstance.contains(String.join(".", keys)) && configFileInstance.get(String.join(".", keys)) != null) {
            configFileInstance.set(String.join(".", keys), value);
            return true;
        }
        return false;
    }

    public static boolean setConfig(String keys, Object value) {
        if (configFileInstance.contains(keys) && configFileInstance.get(keys) != null) {
            configFileInstance.set(keys, value);
            return true;
        }
        return false;
    }

    public static void saveConfig() {
        configFileInstance.save();
    }

    public static void resetConfig(String[] keys) {
        configFileInstance.remove(String.join(".", keys));
        configFileInstance.save();
        reload();
    }

    public static List<String> getAllConfigNames() {
        List<String> list = new ArrayList<>();
        for (CommentedConfig.Entry entry : configFileInstance.entrySet()) list.add(entry.getKey());
        return list;
    }
}
