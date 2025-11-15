package fun.bm.chamomile.data;

import fun.bm.chamomile.data.basedata.BaseDataManager;
import fun.bm.chamomile.data.report.ReportDataManager;
import fun.bm.chamomile.data.unionban.UnionBanDataManager;

public class DataManager {
    public BaseDataManager baseDataManager = new BaseDataManager();
    public ReportDataManager reportDataManager = new ReportDataManager();
    public UnionBanDataManager unionBanDataManager = new UnionBanDataManager();
}
