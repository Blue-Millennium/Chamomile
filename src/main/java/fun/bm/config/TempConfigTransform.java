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
        setConfig("bot.auth.enabled", Config.QQCheckEnabled);
        setConfig("bot.auth.enforce-check", Config.EnforceCheckEnabled);
        setConfig("bot.auth.connect-message", Config.ConnTitle);
        setConfig("bot.auth.disconnect-message", Config.DisTitle);
        setConfig("bot.auth.prefix", Config.QQCheckStartWord);
        setConfig("bot.core.enabled", Config.QQRobotEnabled);
        setConfig("bot.core.ws-url", Config.BotWsUrl);
        setConfig("bot.core.ws-token", Config.BotWsToken);
        setConfig("bot.core.login-check-period", Config.BOTLoginCheckPeriod);
        setConfig("bot.core.official", Config.BotModeOfficial);
        setConfig("bot.rcon.enabled", Config.RconEnabled);
        setConfig("bot.rcon.prefix", Config.ExecuteCommandPrefix);
        setConfig("bot.rcon.enforce-operator", Config.RconEnforceOperator);
        setConfig("bot.rcon.ip", Config.RconIP);
        setConfig("bot.rcon.port", Config.RconPort);
        setConfig("bot.rcon.password", Config.RconPassword);
        setConfig("bot.rcon.enabled-groups", Config.RconEnabledGroups);
        setConfig("bot.report.enabled", true);
        setConfig("bot.report.groups", Config.ReportGroup);
        setConfig("bot.report.message", Config.ReportMessage);
        setConfig("bot.sync-chat.enabled", Config.SyncChatEnabled);
        setConfig("bot.sync-chat.qq-to-server-only", Config.SyncChatEnabledQ2SOnly);
        setConfig("bot.sync-chat.join-message", Config.JoinServerMessage);
        setConfig("bot.sync-chat.leave-message", Config.LeaveServerMessage);
        setConfig("bot.sync-chat.qq-message", Config.SayQQMessage);
        setConfig("bot.sync-chat.server-message", Config.SayServerMessage);
        setConfig("bot.sync-chat.groups", Config.SyncChatGroup);
        setConfig("bot.sync-chat.prefix", Config.SyncChatStartWord);
        saveConfig();
    }
}