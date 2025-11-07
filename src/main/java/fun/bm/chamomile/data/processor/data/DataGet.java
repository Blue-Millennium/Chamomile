package fun.bm.chamomile.data.processor.data;

import fun.bm.chamomile.data.manager.data.Data;
import fun.bm.chamomile.data.manager.data.link.QQLinkData;
import fun.bm.chamomile.data.manager.data.link.UseridLinkData;
import fun.bm.chamomile.util.Environment;

import java.util.List;
import java.util.UUID;

public class DataGet {
    public List<Data> getDataList() {
        return Environment.dataManager.DATA_LIST;
    }

    public List<Data> getPlayersByUserID(long userID) {
        try {
            return getDataList().stream()
                    .filter(record -> record.linkData.stream()
                            .anyMatch(linkData -> linkData instanceof UseridLinkData && ((UseridLinkData) linkData).userid == userID))
                    .toList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Data> getPlayersByQQ(long qqNumber) {
        try {
            return getDataList().stream()
                    .filter(record -> record.linkData.stream()
                            .anyMatch(linkData -> linkData instanceof QQLinkData && ((QQLinkData) linkData).qqNumber == qqNumber))
                    .toList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Data> getPlayersByName(String playerName) {
        try {
            return List.of(Environment.dataManager.getPlayerDataByName(playerName));
        } catch (Exception e) {
            return null;
        }
    }

    public List<Data> getPlayersByUUID(UUID playerUuid) {
        try {
            return List.of(Environment.dataManager.getPlayerData(playerUuid));
        } catch (Exception e) {
            return null;
        }
    }
}
