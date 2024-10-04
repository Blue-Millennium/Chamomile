package fun.suya.suisuroru.rcon;

import org.bukkit.Bukkit;
import org.glavo.rcon.AuthenticationException;
import org.glavo.rcon.Rcon;
import xd.suka.config.Config;

import java.io.IOException;

/**
 * @author Suisuroru
 * Date: 2024/9/28 15:24
 * function: Executes a command through the RCON protocol and returns the console feedback
 */
public class RconCommandExecute {

    private static final int MAX_RETRIES = 3; // 最大重试次数

    public static String executeRconCommand(String command) {
        for (int retry = 0; retry < MAX_RETRIES; retry++) {
            try (Rcon rcon = new Rcon(Config.RconIP, Config.RconPort, Config.RconPassword)) {
                logInfo("Connected to " + Config.RconIP + ":" + Config.RconPort);

                String response = rcon.command(command);
                logInfo("Sent command: " + command);
                // Replace all matching characters (§x)
                String regex = "§.";
                response = response.replaceAll(regex, "");
                logInfo("Received response: " + response);

                return "通过RCON成功执行命令: " + command + "\n" + response.trim();
            } catch (IOException e) {
                logSevere("无法通过RCON执行命令: " + e.getMessage());
                if (retry < MAX_RETRIES - 1) {
                    logInfo("Retrying after 5 seconds...");
                    try {
                        Thread.sleep(5000); // 等待5秒后重试
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (AuthenticationException e) {
                logSevere("RCON认证失败，请检查密码设置。");
                return "RCON认证失败，请检查密码设置。";
            }
        }
        return "所有重试均失败";
    }

    private static void logInfo(String message) {
        Bukkit.getLogger().info(message);
    }

    private static void logSevere(String message) {
        Bukkit.getLogger().severe(message);
    }
}
