package fun.suya.suisuroru.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Suisuroru
 * Date: 2024/10/14 22:41
 * function: Set of all config keys
 */
public enum ConfigKeys {
    QQCheckEnabled(Boolean.class, true),
    SyncChatEnabled(Boolean.class, true),
    BotWsUrl(String.class, "ws://0.0.0.0:3001"),
    BotWsToken(String.class, "114514"),
    SyncChatGroup(Long.class, 123456L),
    ReportGroup(Long.class, 123456L),
    JoinServerMessage(String.class, "%NAME% joined the server"),
    LeaveServerMessage(String.class, "%NAME% left the server"),
    SayServerMessage(String.class, "%NAME%: %MESSAGE%"),
    SayQQMessage(String.class, "[QQ] %NAME%: %MESSAGE%"),
    ReportMessage(String.class, "%NAME% was logging in \nIP: %IP% %IPINFO% \nLoginResult: %RESULT%"),
    DisTitle(String.class, "[QQLogin] 请完成登录验证, 验证码: %CODE%"),
    WebhookUrl(String.class, "http://localhost:6888/webhook"),
    ServerName(String.class, "ServerName"),
    RconEnabled(Boolean.class, true),
    ExecuteCommandPrefix(String.class, "*#"),
    RconEnabledGroups(String.class, "123456;234567"),
    RconIP(String.class, "0.0.0.0"),
    RconPort(Integer.class, 25575),
    RconPassword(String.class, "password"),
    RconEnforceOperator(Boolean.class, true),
    QQRobotEnabled(Boolean.class, true),
    UnionBanEnabled(Boolean.class, false),
    UnionBanCheckOnly(Boolean.class, false),
    UnionBanCheckUrl(String.class, "https://example.com"),
    UnionBanReportUrl(String.class, "https://example.com"),
    UnionBanReportKey(String.class, "your_report_key"),
    QQCheckStartWord(String.class, "Check#");

    public static final Map<String, ConfigKeys> configKeysList = new HashMap<>();

    static {
        for (ConfigKeys key : ConfigKeys.values()) {
            configKeysList.put(key.name(), key);
        }
    }

    private final Class<?> type;
    private final Object defaultValue;

    ConfigKeys(Class<?> type, Object defaultValue) {
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public Class<?> getType() {
        return type;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
