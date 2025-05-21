package fun.bm.config;

import fun.bm.config.old.Config;

import static fun.bm.config.rewritten.ConfigManager.*;

public class TempConfigTransform {
    public static void transformConfig() {
        load();
        setConfig("server.name", Config.ServerName);
        setConfig("server.disable-damage", Config.DamageDisabled);
        setConfig("server.vanilla-commands-rewritten", Config.VanillaCommandsRewritten);
        setConfig("union-ban.enabled", Config.UnionBanEnabled);
        setConfig("union-ban.pull-only", Config.UnionBanCheckOnly);
        setConfig("union-ban.pull-url", Config.UnionBanCheckUrl);
        setConfig("union-ban.report-key", Config.UnionBanReportKey);
        setConfig("union-ban.push-url", Config.UnionBanReportUrl);
        setConfig("union-ban.merge-period", Config.UnionBanMergePeriod);
        setConfig("webhook.enabled", Config.EnableEmailNotice);
        setConfig("webhook.emails", Config.WebHookEmail);
        setConfig("webhook.url", Config.WebhookUrl);
        setConfig("auth.enabled", Config.QQCheckEnabled);
        setConfig("auth.enforce-check", Config.EnforceCheckEnabled);
        setConfig("auth.connect-message", Config.ConnTitle);
        setConfig("auth.disconnect-message", Config.DisTitle);
        setConfig("auth.prefix", Config.QQCheckStartWord);
        setConfig("bot.enabled", Config.QQRobotEnabled);
        setConfig("bot.ws-url", Config.BotWsUrl);
        setConfig("bot.ws-token", Config.BotWsToken);
        setConfig("bot.login-check-period", Config.BOTLoginCheckPeriod);
        setConfig("bot.official", Config.BotModeOfficial);
        setConfig("rcon.enabled", Config.RconEnabled);
        setConfig("rcon.prefix", Config.ExecuteCommandPrefix);
        setConfig("rcon.enforce-operator", Config.RconEnforceOperator);
        setConfig("rcon.ip", Config.RconIP);
        setConfig("rcon.port", Config.RconPort);
        setConfig("rcon.password", Config.RconPassword);
        setConfig("rcon.enabled-groups", Config.RconEnabledGroups);
        setConfig("report.enabled", true);
        setConfig("report.groups", Config.ReportGroup);
        setConfig("report.message", Config.ReportMessage);
        setConfig("sync-chat.enabled", Config.SyncChatEnabled);
        setConfig("sync-chat.qq-to-server-only", Config.SyncChatEnabledQ2SOnly);
        setConfig("sync-chat.join-message", Config.JoinServerMessage);
        setConfig("sync-chat.leave-message", Config.LeaveServerMessage);
        setConfig("sync-chat.qq-message", Config.SayQQMessage);
        setConfig("sync-chat.server-message", Config.SayServerMessage);
        setConfig("sync-chat.groups", Config.SyncChatGroup);
        setConfig("sync-chat.prefix", Config.SyncChatStartWord);
        saveConfig();
    }
}