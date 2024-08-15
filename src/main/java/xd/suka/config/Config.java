package xd.suka.config;

/**
 * @author Liycxc
 * Date: 2024/8/2 下午6:32
 */
public class Config {
    public static Boolean qqCheckEnabled = true;
    public static Boolean syncChatEnabled = true;
    public static String botWsUrl = "ws://172.29.1.20:3001";
    public static String botWsToken = "114514";
    public static long syncChatGroup = 721823314L;
    public static long reportGroup = 721823314L;
    public static String joinServerMessage = "%NAME% joined the server";
    public static String leaveServerMessage = "%NAME% left the server";
    public static String sayServerMessage = "%NAME%: %MESSAGE%";
    public static String sayQQMessage = "[QQ] %NAME%: %MESSAGE%";
    public static String reportMessage = "%NAME% was logging in \nIP: %IP% %IPINFO% \nLoginResult: %RESULT%";
    public static String disTitle = "[QQLogin] 请完成登录验证, 验证码: %CODE%";
}
