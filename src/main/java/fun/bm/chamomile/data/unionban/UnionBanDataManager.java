package fun.bm.chamomile.data.unionban;

public class UnionBanDataManager {
    public LocalDataManager localBanDataManager = new LocalDataManager();
    public OnlineDataManager onlineBanDataManager = new OnlineDataManager();
    public CrossRegionDataManager crossRegionBanDataManager = new CrossRegionDataManager();
}
