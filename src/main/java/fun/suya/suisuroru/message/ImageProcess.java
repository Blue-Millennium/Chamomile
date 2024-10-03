package fun.suya.suisuroru.message;

import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.bukkit.Bukkit;
import xd.suka.config.Config;

public class ImageProcess {

    public static String getImageUrl(MessageChainBuilder builder, Image message) {
        try {
        String imageurl = Image.queryUrl(message);
        Bukkit.getLogger().info("Image url: " + imageurl);
        return imageurl;
        } catch (Exception e){
            Bukkit.getLogger().info("Image url error: " + e.getMessage());
            return "";
        }
    }
}
