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
            URL url = new URL("https://rs.miku39.cloudns.be/https://ipinfo.io/widget/demo/" + ip);
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
            LOGGER.error("Error getting IP info: {}", e.getMessage());
            return null;
        }
    }
}
