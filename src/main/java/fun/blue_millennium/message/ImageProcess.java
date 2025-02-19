package fun.blue_millennium.message;

import fun.blue_millennium.Chamomile;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.Bukkit;

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

    public static String sendImageUrl(Image message) {
        String value = null;
        try {
            String imageurl = getImageUrl(message);
            value = processImageUrl(imageurl);
        } catch (Exception e) {
            Chamomile.INSTANCE.getServer().broadcastMessage("一个错误发生于Chamomile内部，图片无法被展示，请前往控制台查看");
        }
        return value;
    }

    public static String processImageUrl(String imageurl) {
        try {
            MessageChainBuilder builder = new MessageChainBuilder();
            builder.append("[[CICode,url=").append(imageurl).append("]]");
            Bukkit.getLogger().info("Processed image " + imageurl);
            return builder.build().contentToString();
        } catch (Exception e) {
            Chamomile.INSTANCE.getServer().broadcastMessage("一个错误发生于Chamomile内部，图片无法被展示，请前往控制台查看");
        }
        return imageurl;
    }
}
