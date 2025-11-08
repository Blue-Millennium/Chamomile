package fun.bm.chamomile.util.data;

import fun.bm.chamomile.data.basedata.BaseData;
import fun.bm.chamomile.data.basedata.link.QQLinkData;
import fun.bm.chamomile.data.basedata.link.UseridLinkData;
import fun.bm.chamomile.util.Environment;

import java.util.List;
import java.util.UUID;

public class DataGet {
    public List<BaseData> getDataList() {
        return Environment.dataManager.baseDataManager.DATA_LIST;
    }

    public List<BaseData> getPlayersByUserID(long userID) {
        try {
            return getDataList().stream()
                    .filter(record -> record.linkData.stream()
                            .anyMatch(linkData -> linkData instanceof UseridLinkData && ((UseridLinkData) linkData).userid == userID))
                    .toList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<BaseData> getPlayersByQQ(long qqNumber) {
        try {
            return getDataList().stream()
                    .filter(record -> record.linkData.stream()
                            .anyMatch(linkData -> linkData instanceof QQLinkData && ((QQLinkData) linkData).qqNumber == qqNumber))
                    .toList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<BaseData> getPlayersByName(String playerName) {
        try {
            return List.of(Environment.dataManager.baseDataManager.getPlayerDataByName(playerName));
        } catch (Exception e) {
            return null;
        }
    }

    public List<BaseData> getPlayersByUUID(UUID playerUuid) {
        try {
            return List.of(Environment.dataManager.baseDataManager.getPlayerData(playerUuid));
        } catch (Exception e) {
            return null;
        }
    }
}
