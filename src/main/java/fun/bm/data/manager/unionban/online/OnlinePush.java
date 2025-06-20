package fun.bm.data.manager.unionban.online;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fun.bm.config.modules.UnionBanConfig;
import fun.bm.data.manager.unionban.UnionBanData;
import fun.bm.function.modules.UnionBan;
import fun.bm.util.MainEnv;

import static fun.bm.util.HttpUtil.fetch;
import static fun.bm.util.MainEnv.LOGGER;
import static fun.bm.util.helper.EncryptHelper.encrypt;

public class OnlinePush {
    public static boolean reportRemoteBanList(UnionBanData data) {
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
            return false;
        }

        if (base64EncodedJson == null || json_fin == null) {
            return false;
        }

        // 确保 URL 格式正确
        String reportUrl = MainEnv.emailSender.ensureValidUrl(UnionBanConfig.pushUrl);

        byte[] ret = fetch(reportUrl, null, true, json_fin);
        if (ret != null) {
            LOGGER.info("封禁数据上报成功");
            data.reportTag = true;
            UnionBan.unionBanDataGet.setPlayerData(data.playerUuid, data);
            return true;
        } else {
            LOGGER.info("封禁数据上报失败");
            return false;
        }
    }
}
