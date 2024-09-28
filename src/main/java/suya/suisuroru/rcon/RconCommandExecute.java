package suya.suisuroru.rcon;

import org.bukkit.Bukkit;
import xd.suka.config.Config;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author Suisuroru
 * Date: 2024/9/28 15:24p.m.
 * function: Executes a command through the RCON protocol and returns the console feedback
 */
public class RconCommandExecute {

    /**
     * 通过RCON协议执行命令并返回控制台反馈信息
     *
     * @param command 要执行的命令
     * @return 执行结果的信息
     */
    public static String executeRconCommand(String command) {
        try (Socket socket = new Socket(Config.RconIP, Config.RconPort)) {
            socket.setSoTimeout(10000);

            try (DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.writeBytes("login " + Config.RconPassword + "\n");
                out.flush();

                String loginResponse = in.readLine();
                if (!loginResponse.startsWith("Login successful")) {
                    Bukkit.getLogger().info("RCON登录失败: " + loginResponse);
                    return "登录失败: " + loginResponse;
                }

                // 发送实际命令
                out.writeBytes(command + "\n");
                out.flush();

                // 读取命令执行后的反馈信息
                StringBuilder feedback = new StringBuilder();
                String response;
                boolean done = false;
                long startTime = System.currentTimeMillis();
                while (!done && (response = in.readLine()) != null) {
                    if (System.currentTimeMillis() - startTime > 9000) { // 额外等待1秒
                        throw new IOException("Read timed out");
                    }
                    feedback.append(response).append("\n");
                    Bukkit.getLogger().info(response);
                    if (response.startsWith("Done")) {
                        done = true;
                    }
                }

                String result = "通过RCON成功执行命令: " + command + "\n" + feedback.toString().trim();
                Bukkit.getLogger().info(result);
                return result;

            } catch (IOException e) {
                Bukkit.getLogger().severe("无法通过RCON执行命令: " + e.getMessage());
                return "无法通过RCON执行命令: " + e.getMessage();
            }

        } catch (IOException e) {
            Bukkit.getLogger().severe("Socket连接问题: " + e.getMessage());
            return "Socket连接问题: " + e.getMessage();
        }
    }

}
