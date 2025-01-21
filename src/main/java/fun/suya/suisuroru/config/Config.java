package fun.suya.suisuroru.config;

/**
 * @author Suisuroru
 * Date: 2024/10/14 22:49
 * function: Initialize config format
 */
public class Config {
    public static boolean VanillaCommandsRewritten = true;
    public static boolean QQCheckEnabled = true;
    public static boolean SyncChatEnabled = true;
    public static boolean SyncChatEnabledQ2SOnly = false;
    public static String BotWsUrl = "ws://0.0.0.0:3001";
    public static String BotWsToken = "114514";
    public static long SyncChatGroup = 123456L;
    public static long ReportGroup = 123456L;
    public static String JoinServerMessage = "%NAME% joined the server";
    public static String LeaveServerMessage = "%NAME% left the server";
    public static String SayServerMessage = "%NAME%: %MESSAGE%";
    public static String SayQQMessage = "[QQ] %NAME%: %MESSAGE%";
    public static String ReportMessage = "%NAME% was logging in \nIP: %IP% %IPINFO% \nLoginResult: %RESULT%";
    public static String DisTitle = "[QQLogin] 请完成登录验证, 验证码: %CODE%";
    public static String WebhookUrl = "http://localhost:6888/webhook";
    public static String ServerName = "ServerName";
    public static boolean RconEnabled = true;
    public static String ExecuteCommandPrefix = "*#";
    public static String RconEnabledGroups = "123456;234567";
    public static String RconIP = "0.0.0.0";
    public static int RconPort = 25575;
    public static String RconPassword = "password";
    public static boolean RconEnforceOperator = true;
    public static boolean QQRobotEnabled = true;
    public static boolean UnionBanEnabled = false;
    public static boolean UnionBanCheckOnly = false;
    public static String UnionBanCheckUrl = "https://example.com";
    public static String UnionBanReportUrl = "https://example.com";
    public static String UnionBanReportKey = "your_report_key";
    public static String QQCheckStartWord = "Check#";
}