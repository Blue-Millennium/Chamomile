package fun.blue_millennium.data.UnionBan.LocalProcess;

import fun.blue_millennium.data.UnionBan.UnionBanData;

import java.util.List;

public class BanDataProcess {
    public static void banDataProcess() {
        UnionBanDataGet dg = new UnionBanDataGet();
        List<UnionBanData> dataList = dg.returnAllData();
        for (UnionBanData data : dataList) {

        }
    }
}
