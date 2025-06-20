package fun.bm.data.manager.unionban.online;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.bm.config.modules.UnionBanConfig;
import fun.bm.data.manager.unionban.UnionBanData;
import fun.bm.util.MainEnv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static fun.bm.util.HttpUtil.fetch;
import static fun.bm.util.MainEnv.LOGGER;

public class OnlineGet {
    public static ArrayList<UnionBanData> loadRemoteBanList() {
        ArrayList<UnionBanData> banList = new ArrayList<>();
        // 确保 URL 格式正确
        String checkUrl = MainEnv.emailSender.ensureValidUrl(UnionBanConfig.pullUrl);

        try {
            byte[] data = fetch(checkUrl, null, false, null);
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
}
