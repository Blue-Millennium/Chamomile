package xd.suka.util;

public class TimeUtil {
    public static String getNowTime(){
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }
}
