package fun.bm.util;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import fun.bm.util.map.IpLocationMap;
import fun.bm.util.map.IpinfoMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static fun.bm.util.helper.MainEnv.LOGGER;

public class IpInfoUtil {
    public static IpinfoMap getIpinfo(String ip) {
        try {
            URL url = new URL("https://ipinfo.io/widget/demo/" + ip);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            return new Gson().fromJson(response.toString(), IpinfoMap.class);
        } catch (Exception e) {
            LOGGER.warning("Error getting IP info: " + e.getMessage());
            return null;
        }
    }

    public static IpLocationMap getIpinfoCN(String ip) {
        try {
            URL url = new URL("https://webapi-pc.meitu.com/common/ip_location?ip=" + ip);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            return new Gson().fromJson(JsonParser.parseString(response.toString()).getAsJsonObject().get("data").getAsJsonObject().get(ip), IpLocationMap.class);
        } catch (IOException e) {
            LOGGER.warning("Error getting IP CN info: " + e.getMessage());
        }
        return null;
    }
}
