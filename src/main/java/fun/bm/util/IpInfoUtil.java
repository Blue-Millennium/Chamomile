package fun.bm.util;

import com.google.gson.Gson;
import fun.bm.util.map.IpLocationMap;
import fun.bm.util.map.IpinfoMap;

import static fun.bm.util.HttpUtil.fetch;
import static fun.bm.util.MainEnv.LOGGER;

public class IpInfoUtil {
    public static IpinfoMap getIpinfo(String ip) {
        String url = "https://ipinfo.io/widget/demo/" + ip;
        return getInfo(url, IpinfoMap.class);
    }

    public static IpLocationMap getIpinfoCN(String ip) {
        String url = "https://webapi-pc.meitu.com/common/ip_location?ip=" + ip;
        return getInfo(url, IpLocationMap.class);
    }

    public static <T> T getInfo(String url, Class<T> responseType) {
        try {
            byte[] bytes = fetch(url, null, false, null);
            if (bytes == null) return null;

            String jsonStr = new String(bytes);
            return new Gson().fromJson(jsonStr, responseType);
        } catch (Exception e) {
            LOGGER.warning("Error getting IP info: " + e.getMessage());
            return null;
        }
    }

}
