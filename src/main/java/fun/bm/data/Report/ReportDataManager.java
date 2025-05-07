package fun.bm.data.Report;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static fun.bm.data.DataProcessor.Data.DataStringBuilder.transferTime;
import static fun.bm.util.MainEnv.LOGGER;
import static fun.bm.util.TimeUtil.getUnixTime;

/**
 * @author Suisuroru
 * Date: 2024/9/29 16:02
 * function: Manage report data
 */
public class ReportDataManager {

    public static boolean deleteData(String timestamp) {
        List<List<String>> reportData = ReportDataActions.ReadReportFile();
        try {
            reportData.removeIf(row -> row.get(0).equals(timestamp));
            ReportDataActions.saveToCsv(reportData);
            return true;
        } catch (Exception e) {
            LOGGER.warning("Failed to delete data from CSV file: " + e.getMessage());
            return false;
        }
    }

    /**
     * 接收列表并将数据追加到CSV文件中。
     *
     * @param data 新的数据行
     */
    public void appendDataToCsv(List<String> data) {
        ReportDataActions.writeNewData(data);
    }

    public void ProcessData(@NotNull CommandSender sender, @NotNull String[] args) {
        List<String> ProcessData = new ArrayList<>();

        long time = getUnixTime();
        ProcessData.add(String.valueOf(time));
        ProcessData.add(transferTime(time));

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
