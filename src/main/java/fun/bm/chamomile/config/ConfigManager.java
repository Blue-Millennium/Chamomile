package fun.bm.chamomile.config;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import fun.bm.chamomile.config.flags.ConfigInfo;
import fun.bm.chamomile.config.flags.DoNotLoad;
import fun.bm.chamomile.config.flags.DoNotReload;
import fun.bm.chamomile.config.flags.TransformedConfig;
import fun.bm.chamomile.util.MainEnv;
import fun.bm.chamomile.util.helper.ClassLoadHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static fun.bm.chamomile.util.MainEnv.LOGGER;

public class ConfigManager {
    private static final Set<ConfigModule> configModules = new HashSet<>();
    private static final Map<String, Object> stagedConfigMap = new HashMap<>();
    private static final Map<String, Object> defaultvalueMap = new HashMap<>();
    private static CommentedFileConfig commentedFileConfig;

    private static Object tryTransform(Class<?> targetType, Object value) {
        if (!targetType.isAssignableFrom(value.getClass())) {
            try {
                if (targetType == Integer.class) {
                    value = Integer.parseInt(value.toString());
                } else if (targetType == Double.class) {
                    value = Double.parseDouble(value.toString());
                } else if (targetType == Boolean.class) {
                    value = Boolean.parseBoolean(value.toString());
                } else if (targetType == Long.class) {
                    value = Long.parseLong(value.toString());
                } else if (targetType == Float.class) {
                    value = Float.parseFloat(value.toString());
                } else if (targetType == String.class) {
                    value = value.toString();
                }
            } catch (Exception e) {
                LOGGER.warning("Failed to transform value " + value + "!");
                throw new IllegalFormatConversionException((char) 0, targetType);
            }
        }
        return value;
    }

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
        configModules.clear();
        baseload();
        loadConfigModule(true);
    }

    private void loadConfigModule(boolean reload) {
        configModules.addAll(ClassLoadHelper.loadClasses("fun.bm.chamomile.config.modules", ConfigModule.class));

        for (ConfigModule module : configModules) {
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
                    if (!reload) defaultvalueMap.put(fullConfigKeyName, currentValue);
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
                        if (removed) {
                            commentedFileConfig.remove("removed");
                            continue;
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

                    Object actuallyValue;
                    if (stagedConfigMap.containsKey(fullConfigKeyName)) {
                        actuallyValue = stagedConfigMap.get(fullConfigKeyName);
                        if (actuallyValue == null) actuallyValue = defaultvalueMap.get(fullConfigKeyName);
                        stagedConfigMap.remove(fullConfigKeyName);
                    } else {
                        actuallyValue = commentedFileConfig.get(fullConfigKeyName);
                    }
                    try {
                        actuallyValue = tryTransform(field.get(null).getClass(), actuallyValue);
                        commentedFileConfig.set(fullConfigKeyName, actuallyValue);
                    } catch (IllegalFormatConversionException e) {
                        resetConfig(fullConfigKeyName);
                        LOGGER.warning("Failed to transform config " + fullConfigKeyName + ", reset to default!");
                    } catch (IllegalAccessException e) {
                        LOGGER.warning("Failed to get config value for " + field.getName() + ": " + e.getMessage());
                    }
                    try {
                        field.set(null, actuallyValue);
                    } catch (IllegalAccessException e) {
                        LOGGER.warning("Failed to set config value for " + field.getName() + ": " + e.getMessage());
                    }
                }
            }
        }

        saveConfigs();
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
            saveConfigs();
            return true;
        }
        return false;
    }

    public boolean setConfig(String[] keys, Object value) {
        return setConfig(String.join(".", keys), value);
    }

    public boolean setConfig(String key, Object value) {
        if (commentedFileConfig.contains(key) && commentedFileConfig.get(key) != null) {
            stagedConfigMap.put(key, value);
            return true;
        }
        return false;
    }

    public void saveConfigs() {
        commentedFileConfig.save();
    }

    public void resetConfig(String[] keys) {
        resetConfig(String.join(".", keys));
    }

    public void resetConfig(String key) {
        stagedConfigMap.put(key, null);
        reload();
    }

    public String getConfig(String[] keys) {
        return getConfig(String.join(".", keys));
    }

    public String getConfig(String key) {
        return commentedFileConfig.get(key).toString();
    }

    public List<String> completeConfigPath(String partialPath) {
        List<String> allPaths = getAllConfigPaths(partialPath);
        List<String> result = new ArrayList<>();

        for (String path : allPaths) {
            String remaining = path.substring(partialPath.length());
            if (remaining.isEmpty()) continue;

            int dotIndex = remaining.indexOf('.');
            String suggestion = (dotIndex == -1)
                    ? path
                    : partialPath + remaining.substring(0, dotIndex);

            if (!result.contains(suggestion)) {
                result.add(suggestion);
            }
        }
        return result;
    }

    public List<String> getAllConfigPaths(String currentPath) {
        return defaultvalueMap.keySet().stream()
                .filter(k -> k.startsWith(currentPath))
                .toList();
    }
}
