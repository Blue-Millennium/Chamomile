package fun.bm.util.helper;

import fun.bm.util.MainEnv;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;

/**
 * @author Suisuroru
 * Date: 2024/10/3 21:58
 * function: Process image message
 */
public class ImageProcessor {

    public static String getImageUrl(Image message) {
        String imageurl = Image.queryUrl(message);
        return imageurl;
    }

    public static String sendImageUrl(Image message) {
        String value = null;
        try {
            String imageurl = getImageUrl(message);
            value = processImageUrl(imageurl);
        } catch (Exception e) {
            MainEnv.INSTANCE.getServer().broadcastMessage("一个错误发生于Chamomile内部，图片无法被展示，请前往控制台查看");
        }
        return value;
    }

    public static String processImageUrl(String imageurl) {
        try {
            MessageChainBuilder builder = new MessageChainBuilder();
            builder.append("[[CICode,url=").append(imageurl).append("]]");
            return builder.build().contentToString();
        } catch (Exception e) {
            MainEnv.INSTANCE.getServer().broadcastMessage("一个错误发生于Chamomile内部，图片无法被展示，请前往控制台查看");
        }
        return imageurl;
    }
}
