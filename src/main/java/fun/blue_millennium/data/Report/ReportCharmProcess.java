package fun.blue_millennium.data.Report;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static fun.blue_millennium.Chamomile.BASE_DIR;
import static fun.blue_millennium.Chamomile.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/10/4 16:21
 * function: Process the report data to charm image
 */
public class ReportCharmProcess {
    public static void reportCharmProcess(String message) {
        String[] lines = message.split("\n");
        int lineHeight = 20; // 每行的高度为20像素
        int fontWidth = 10; // 每个字符的宽度为10像素
        int columnPadding = 2; // 列之间的填充
        int borderWidth = 2; // 边框宽度
        int firstColumnWidth = 30; // 第一列宽度
        int otherColumnWidth = 200; // 其他列宽度
        int width = 0;
        int height;

        // 计算最大宽度
        for (String line : lines) {
            String[] columns = line.split("\\|");
            width = Math.max(width, firstColumnWidth + (columns.length - 1) * (otherColumnWidth + columnPadding));
        }
        width += borderWidth * 2; // 加上两边边框
        height = (lines.length + 1) * lineHeight + borderWidth * 2; // 加上上下边框

        // 创建一个白色背景的图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, width, height);
        g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        g2d.setColor(Color.BLACK);

        // 绘制水平线
        g2d.drawLine(borderWidth, 0, width - borderWidth, 0);
        g2d.drawLine(borderWidth, height - borderWidth, width - borderWidth, height - borderWidth);

        int y = borderWidth;
        for (String line : lines) {
            String[] columns = line.split("\\|");
            int x = borderWidth;

            // 绘制垂直线
            g2d.drawLine(x, borderWidth, x, height - borderWidth);

            for (int i = 0; i < columns.length; i++) {
                String col = columns[i].trim();
                int currentColumnWidth = (i == 0) ? firstColumnWidth : otherColumnWidth;

                // 计算文本居中位置
                int textX = x + columnPadding / 2 + (currentColumnWidth - col.length() * fontWidth) / 2;
                g2d.drawString(col, textX, y + lineHeight - 5); // 调整y坐标使文本居中
                x += currentColumnWidth + columnPadding;

                if (i < columns.length - 1) { // 不绘制最后一列的右边线
                    g2d.drawLine(x, borderWidth, x, height - borderWidth);
                }
            }

            y += lineHeight;
            // 绘制行间水平线
            g2d.drawLine(borderWidth, y, width - borderWidth, y);
        }

        g2d.dispose();

        // 保存图片到指定路径
        File outputDir = new File(BASE_DIR, "CharmProcess");
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            LOGGER.warning("Failed to create directory: " + outputDir.getAbsolutePath());
        }

        try {
            File outputFile = new File(outputDir, "latest.png");
            ImageIO.write(image, "png", outputFile);
            LOGGER.info("Image saved to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.severe("Failed to write image: " + e.getMessage());
            LOGGER.warning(e.getMessage());
        }
    }
}
