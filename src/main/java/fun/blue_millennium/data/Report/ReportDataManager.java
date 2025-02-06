package fun.blue_millennium.data.Report;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Suisuroru
 * Date: 2024/9/29 16:02
 * function: Manage report data
 */
public class ReportDataManager {

    /**
     * 接收列表并将数据追加到CSV文件中。
     *
     * @param data 新的数据行
     */
    public void appendDataToCsv(List<String> data) {
        ReportDataActions writer = new ReportDataActions();
        writer.writeNewData(data);
    }

    public void ProcessData(@NotNull CommandSender sender, @NotNull String[] args) {
        List<String> ProcessData = new ArrayList<>();

        // 获取当前时间戳并转换为字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new Date());
        ProcessData.add(timestamp);

        // 将 CommandSender 转换为游戏内的名字
        if (sender instanceof org.bukkit.entity.Player player) {
            ProcessData.add(player.getName());
        } else {
            ProcessData.add(sender.getName());
        }

        // 提取被举报人的名字和举报原因
        if (args.length > 0) {
            String reportedPlayerName = args[0];
            StringBuilder reason = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                reason.append(args[i]).append(" ");
            }
            ProcessData.add(reportedPlayerName);
            ProcessData.add(reason.toString().trim());
        }

        appendDataToCsv(ProcessData);
    }
}
