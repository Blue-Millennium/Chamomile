package fun.suya.suisuroru.message;

import fun.suya.suisuroru.config.Config;
import fun.xd.suka.Main;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.Bukkit;

import static fun.suya.suisuroru.rcon.RconCommandExecute.executeRconCommand;

/**
 * @author Suisuroru
 * Date: 2024/10/3 21:58
 * function: Process image message
 */
public class ImageProcess {

    public static String getImageUrl(Image message) {
        String imageurl = Image.queryUrl(message);
        Bukkit.getLogger().info("Image url: " + imageurl);
        return imageurl;
    }

    public static void sendImageUrl(Image message, GroupMessageEvent event) {
        try {
            String imageurl = getImageUrl(message);
            MessageChainBuilder builder = new MessageChainBuilder();
            builder.append("[[CICode,url=").append(imageurl).append("]]");
            // mohist端BUG导致，使用tellraw代替直接发送
            String command = "tellraw @a \"" + Config.SayQQMessage.replace("%NAME%", event.getSenderName()).replace("%MESSAGE%", builder.build().contentToString()) + "\"";
            executeRconCommand(Config.RconIP, Config.RconPort, Config.RconPassword, command);
        } catch (Exception e) {
            Main.INSTANCE.getServer().broadcastMessage("一个错误发生于BasePlugin内部，图片无法被展示，请前往控制台查看");
        }
    }
}
