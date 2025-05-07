package fun.bm.data.DataProcessor.LinkData;

import fun.bm.data.LoginData.Data;
import fun.bm.data.LoginData.LinkData.LinkData;
import fun.bm.data.LoginData.LinkData.QQLinkData;
import fun.bm.data.LoginData.LinkData.UseridLinkData;
import fun.bm.util.MainEnv;

import java.util.List;

import static fun.bm.data.DataProcessor.Data.DataStringBuilder.appendIfNotNull;
import static fun.bm.data.DataProcessor.Data.DataStringBuilder.transferTime;

public class LinkDataStringBuilder {
    public static String buildLinkDataString(String name) {
        Data data = MainEnv.dataManager.getPlayerDataByName(name);
        StringBuilder result = new StringBuilder();
        if (data != null) {
            List<LinkData> linkDataList = data.linkData;
            if (linkDataList != null) {
                appendIfNotNull(result, "§a-------------------");
                if (data.linkData.size() > 1) {
                    appendIfNotNull(result, "§a查询到多个绑定数据，共 " + data.linkData.size() + " 个");
                    appendIfNotNull(result, "§a-------------------");
                }
                int i = 1;
                for (LinkData linkData : linkDataList) {
                    appendIfNotNull(result, "§a查询到第 " + i++ + " 个绑定数据");
                    if (linkData instanceof QQLinkData qqLinkData) {
                        appendIfNotNull(result, "QQ号码: ", qqLinkData.qqNumber);
                    } else if (linkData instanceof UseridLinkData useridLinkData) {
                        appendIfNotNull(result, "UserID识别码: ", useridLinkData.userid);
                        appendIfNotNull(result, "UserID绑定的群聊: ", useridLinkData.useridLinkedGroup);
                    }
                    appendIfNotNull(result, "绑定时间: ", transferTime(linkData.linkedTime));
                    appendIfNotNull(result, "绑定时间戳: ", linkData.linkedTime);
                    appendIfNotNull(result, "§a-------------------");
                }
            }
        }
        return result.toString();
    }
}
