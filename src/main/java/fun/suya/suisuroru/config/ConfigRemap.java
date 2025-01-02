package fun.suya.suisuroru.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Suisuroru
 * Date: 2024/10/14 22:49
 * function: Provide a mapping between config keys and config values
 */
public class ConfigRemap {
    public static final Map<String, String> configMapping = new HashMap<>();

    // configMapping.put(ConfigKeys, Config);
    static {
        configMapping.put("VanillaCommandsRewritten", "VanillaCommandsRewritten");
        configMapping.put("QQCheckEnabled", "qqCheckEnabled");
        configMapping.put("SyncChatEnabled", "syncChatEnabled");
        configMapping.put("BotWsUrl", "botWsUrl");
        configMapping.put("BotWsToken", "botWsToken");
        configMapping.put("SyncChatGroup", "syncChatGroup");
        configMapping.put("ReportGroup", "reportGroup");
        configMapping.put("JoinServerMessage", "joinServerMessage");
        configMapping.put("LeaveServerMessage", "leaveServerMessage");
        configMapping.put("SayServerMessage", "sayServerMessage");
        configMapping.put("SayQQMessage", "sayQQMessage");
        configMapping.put("ReportMessage", "reportMessage");
        configMapping.put("DisTitle", "disTitle");
        configMapping.put("WebhookUrl", "webhookUrl");
        configMapping.put("ServerName", "servername");
        configMapping.put("RconEnabled", "RconEnabled");
        configMapping.put("ExecuteCommandPrefix", "ExecuteCommandPrefix");
        configMapping.put("RconEnabledGroups", "RconEnabledGroups");
        configMapping.put("RconIP", "RconIP");
        configMapping.put("RconPort", "RconPort");
        configMapping.put("RconPassword", "RconPassword");
        configMapping.put("RconEnforceOperator", "RconEnforceOperator");
        configMapping.put("QQRobotEnabled", "QQRobotEnabled");
        configMapping.put("UnionBanEnabled", "UnionBanEnabled");
        configMapping.put("UnionBanCheckOnly", "UnionBanCheckOnly");
        configMapping.put("UnionBanCheckUrl", "UnionBanCheckUrl");
        configMapping.put("UnionBanReportUrl", "UnionBanReportUrl");
        configMapping.put("UnionBanReportKey", "UnionBanReportKey");
        configMapping.put("QQCheckStartWord", "QQCheckStartWord");
    }
}