package fun.bm.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import fun.bm.config.flags.ConfigInfo;
import fun.bm.config.flags.DoNotLoad;
import fun.bm.config.flags.DoNotReload;
import fun.bm.config.flags.TransformedConfig;
import fun.bm.util.MainEnv;
import fun.bm.util.helper.ClassLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static fun.bm.util.MainEnv.LOGGER;

public class ConfigManager {
    private static final Set<ConfigModule> ConfigModules = new HashSet<>();
    private static CommentedFileConfig commentedFileConfig;

    public void load() {
        baseload();
        loadConfigModule(false);
    }

    public void baseload() {
        try {
            MainEnv.BASE_DIR.mkdir();
            if (!MainEnv.CONFIG_FILE.exists()) MainEnv.CONFIG_FILE.createNewFile();
        } catch (Exception e) {
            LOGGER.warning("Failed to create config file");
        }
        commentedFileConfig = CommentedFileConfig.of(MainEnv.CONFIG_FILE);
        commentedFileConfig.load();
    }

    public void reload() {
        commentedFileConfig.clear();
        ConfigModules.clear();
        baseload();
        loadConfigModule(true);
    }

    private void loadConfigModule(boolean reload) {
        ConfigModules.addAll(ClassLoader.loadClasses("fun.bm.config.modules", ConfigModule.class));

        for (ConfigModule module : ConfigModules) {
            Field[] fields = module.getClass().getDeclaredFields();

            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
                    boolean skipLoad = field.getAnnotation(DoNotLoad.class) != null || (reload && field.getAnnotation(DoNotReload.class) != null);
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

                    if (!commentedFileConfig.contains(fullConfigKeyName) || removed) {
                        for (TransformedConfig transformedConfig : field.getAnnotationsByType(TransformedConfig.class)) {
                            final String oldConfigKeyName = String.join(".", transformedConfig.category()) + "." + transformedConfig.name();
                            Object oldValue = commentedFileConfig.get(oldConfigKeyName);
                            if (oldValue != null) {
                                boolean success = true;
                                if (transformedConfig.transform() && !removed) {
                                    try {
                                        for (Class<? extends DefaultTransformLogic> logic : transformedConfig.transformLogic()) {
                                            oldValue = logic.getDeclaredConstructor().newInstance().transform(oldValue);
                                        }
                                        commentedFileConfig.add(fullConfigKeyName, oldValue);
                                    } catch (Exception e) {
                                        success = false;
                                        LOGGER.warning("Failed to transform removed config " + transformedConfig.name() + "!");
                                    }
                                }

                                if (success) removeConfig(oldConfigKeyName, transformedConfig.category());
                                final String comments = configInfo.comment();

                                if (!comments.isBlank()) commentedFileConfig.setComment(fullConfigKeyName, comments);

                                if (!removed && commentedFileConfig.get(fullConfigKeyName) != null) break;
                            }
                        }
                        if (commentedFileConfig.get(fullConfigKeyName) != null) continue;
                        if (currentValue == null) {
                            LOGGER.warning("Config " + module.name() + "tried to add an null default value!");
                            continue;
                        }

                        final String comments = configInfo.comment();

                        if (!comments.isBlank()) {
                            commentedFileConfig.setComment(fullConfigKeyName, comments);
                        }

                        commentedFileConfig.add(fullConfigKeyName, currentValue);
                        continue;
                    }

                    final Object actuallyValue = commentedFileConfig.get(fullConfigKeyName);
                    try {
                        field.set(null, actuallyValue);
                    } catch (IllegalAccessException e) {
                        LOGGER.warning("Failed to set config value for " + field.getName() + ": " + e.getMessage());
                    }
                }
            }
        }

        commentedFileConfig.save();
    }

    private void removeConfig(String name, String[] keys) {
        commentedFileConfig.remove(name);
        Object configAtPath = commentedFileConfig.get(String.join(".", keys));
        if (configAtPath instanceof UnmodifiableConfig && ((UnmodifiableConfig) configAtPath).isEmpty()) {
            removeConfig(keys);
        }
    }

    private void removeConfig(String[] keys) {
        commentedFileConfig.remove(String.join(".", keys));
        Object configAtPath = commentedFileConfig.get(String.join(".", Arrays.copyOfRange(keys, 1, keys.length)));
        if (configAtPath instanceof UnmodifiableConfig && ((UnmodifiableConfig) configAtPath).isEmpty()) {
            removeConfig(Arrays.copyOfRange(keys, 1, keys.length));
        }
    }

    public boolean setConfigAndSave(String[] keys, Object value) {
        return setConfigAndSave(String.join(".", keys), value);
    }

    public boolean setConfigAndSave(String key, Object value) {
        if (setConfig(key, value)) {
            saveConfig();
            return true;
        }
        return false;
    }

    public boolean setConfig(String[] keys, Object value) {
        return setConfig(String.join(".", keys), value);
    }

    public boolean setConfig(String key, Object value) {
        if (commentedFileConfig.contains(key) && commentedFileConfig.get(key) != null) {
            commentedFileConfig.set(key, value);
            return true;
        }
        return false;
    }

    public void saveConfig() {
        commentedFileConfig.save();
    }

    public void resetConfig(String[] keys) {
        resetConfig(String.join(".", keys));
    }

    public void resetConfig(String key) {
        commentedFileConfig.remove(key);
        commentedFileConfig.save();
        reload();
    }

    public String getConfig(String[] keys) {
        return getConfig(String.join(".", keys));
    }

    public String getConfig(String key) {
        return commentedFileConfig.get(key);
    }

    public List<String> getAllConfigPaths() {
        List<String> configPaths = new ArrayList<>();
        getAllConfigPathsRecursive(commentedFileConfig, "", configPaths);
        return configPaths;
    }

    private void getAllConfigPathsRecursive(CommentedConfig config, String currentPath, List<String> configPaths) {
        for (CommentedConfig.Entry entry : config.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String fullPath = currentPath.isEmpty() ? key : currentPath + "." + key;

            if (value instanceof CommentedConfig) {
                getAllConfigPathsRecursive((CommentedConfig) value, fullPath, configPaths);
            } else {
                configPaths.add(fullPath);
            }
        }
    }
}
