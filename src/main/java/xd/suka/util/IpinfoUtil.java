package xd.suka.util;

import com.google.gson.Gson;
import xd.suka.util.map.IpinfoMap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static xd.suka.Main.LOGGER;

public class IpinfoUtil {
    public static IpinfoMap getIpinfo(String ip) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://ipinfo.io/widget/demo/" + ip).openConnection();

            // 设置请求方法为GET
            connection.setRequestMethod("GET");

            // 读取响应内容
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            // 关闭连接和输入流
            in.close();
            connection.disconnect();

            IpinfoMap ipinfoMap = new Gson().fromJson(content.toString(), IpinfoMap.class);

            if (!ipinfoMap.error.isEmpty()) {
                LOGGER.warn("Warn getting {} info: {}", ipinfoMap.input, ipinfoMap.error);
            }

            return ipinfoMap;
        } catch (Exception e) {
            LOGGER.error("Error getting IP info: {}", e.getMessage());
            return null;
        }
    }
}
