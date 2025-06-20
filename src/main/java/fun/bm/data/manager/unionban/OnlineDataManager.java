package fun.bm.data.manager.unionban;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fun.bm.config.modules.UnionBanConfig;
import fun.bm.function.modules.UnionBan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static fun.bm.util.HttpUtil.fetch;
import static fun.bm.util.MainEnv.LOGGER;
import static fun.bm.util.helper.EncryptHelper.encrypt;

public class OnlineDataManager {
    public ArrayList<UnionBanData> loadRemoteBanList() {
        ArrayList<UnionBanData> banList = new ArrayList<>();
        try {
            byte[] data = fetch(UnionBanConfig.pullUrl, null, false, null);
            if (data == null) return banList;
            String jsonResponse = new String(data);
            ObjectMapper objectMapper = new ObjectMapper();
            List<UnionBanData> remoteBans = objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, UnionBanData.class));
            banList.addAll(remoteBans);
        } catch (IOException e) {
            LOGGER.info(String.valueOf(e));
        }
        return banList;
    }

    public boolean reportRemoteBanList(UnionBanData data) {
        String json = buildJson(data);
        if (json == null) return false;
        byte[] ret = fetch(UnionBanConfig.pushUrl, null, true, json);
        if (ret != null) {
            LOGGER.info("封禁数据上报成功");
            data.reportTag = true;
            UnionBan.localBanDataManager.setPlayerData(data.playerUuid, data);
            return true;
        } else {
            LOGGER.info("封禁数据上报失败");
            return false;
        }
    }

    private String buildJson(UnionBanData data) {
        // 创建 JSON 数据
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode banDataNode = objectMapper.createObjectNode();
        banDataNode.put("playerName", String.valueOf(data.playerName));
        banDataNode.put("playerUuid", String.valueOf(data.playerUuid));
        banDataNode.put("reason", data.reason);
        banDataNode.put("time", data.time);
        banDataNode.put("sourceServer", data.sourceServer);
        String base64EncodedJson;
        String json_fin;
        try {
            String json = objectMapper.writeValueAsString(banDataNode);
            base64EncodedJson = encrypt(json, UnionBanConfig.reportKey);
            ObjectNode dataNode_new = objectMapper.createObjectNode();
            dataNode_new.put("data", base64EncodedJson);
            json_fin = objectMapper.writeValueAsString(dataNode_new);
        } catch (JsonProcessingException e) {
            LOGGER.warning("Failed to convert UnionBan data to JSON: " + e.getMessage());
            return null;
        }

        if (base64EncodedJson == null || json_fin == null) {
            return null;
        }
        return json_fin;
    }
}
