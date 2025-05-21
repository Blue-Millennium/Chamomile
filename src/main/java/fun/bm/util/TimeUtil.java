package fun.bm.util;

public class TimeUtil {
    public static String getNowTime() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }

    public static long getUnixTimeS() {
        return System.currentTimeMillis() / 1000;
    }

    public static long getUnixTimeMs() {
        return System.currentTimeMillis();
    }
}
