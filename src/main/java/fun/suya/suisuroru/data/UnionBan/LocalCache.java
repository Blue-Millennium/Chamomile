package fun.suya.suisuroru.data.UnionBan;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.suya.suisuroru.config.Config;
import fun.suya.suisuroru.module.impl.UnionBan;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static xd.suka.Main.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/10/29 09:37
 * function: Cache the data when offline
 */
public class LocalCache {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void save(UnionBan.BanPair<UUID, String, Date, String> pair) {
        File cacheDir = initial();
        File banData = new File(cacheDir, "cached-data.json");

        Map<String, Object> existingData = readJsonFile(banData);
        List<Map<String, Object>> existingPairs = (List<Map<String, Object>>) existingData.get("pairs");
        if (existingPairs == null) {
            existingPairs = new ArrayList<>();
        }

        Map<String, Object> newData = new HashMap<>();
        newData.put("uuid", pair.getUUID().toString());
        newData.put("reason", pair.getReason());
        newData.put("time", pair.getTime().toString());
        Object serverData;
        if (pair.getSourceServer().equals("Local")) {
            serverData = Config.servername;
        } else {
            serverData = pair.getSourceServer();
        }
        newData.put("sourceServer", serverData);

        existingPairs.add(newData);

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("pairs", existingPairs);

        writeJsonFile(banData, updatedData);
    }

    public static ArrayList<UnionBan.BanPair<UUID, String, Date, String>> read() {
        File cacheDir = initial();
        File banData = new File(cacheDir, "cached-data.json");

        Map<String, Object> data = readJsonFile(banData);
        List<Map<String, Object>> pairs = (List<Map<String, Object>>) data.get("pairs");

        if (pairs == null) {
            return new ArrayList<>();
        }

        ArrayList<UnionBan.BanPair<UUID, String, Date, String>> result = new ArrayList<>();
        for (Map<String, Object> pairData : pairs) {
            String uuid = (String) pairData.get("uuid");
            String reason = (String) pairData.get("reason");
            long time = (long) pairData.get("time");
            String sourceServer = (String) pairData.get("sourceServer");

            result.add(new UnionBan.BanPair(UUID.fromString(uuid), reason, new Date(time), sourceServer));
        }

        return result;
    }

    private static File initial() {
        File cacheDir = new File("BasePlugin/UnionBan");
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new RuntimeException("Failed to create directory: " + cacheDir.getAbsolutePath());
        }
        return cacheDir;
    }

    private static Map<String, Object> readJsonFile(File file) {
        try {
            if (!file.exists()) {
                return new HashMap<>();
            }
            return objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            LOGGER.severe("Failed to read JSON file: " + file.getAbsolutePath());
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private static void writeJsonFile(File file, Map<String, Object> data) {
        try {
            objectMapper.writeValue(file, data);
        } catch (IOException e) {
            LOGGER.severe("Failed to write JSON file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }
}
