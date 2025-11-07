package fun.bm.chamomile.command.modules.executor.extra.sub.report;

import fun.bm.chamomile.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static fun.bm.chamomile.util.Environment.LOGGER;
import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

/**
 * @author Suisuroru
 * Date: 2024/9/29 22:08
 * function: Query report data
 */
public class ReportQuery extends Command.ExecutorE {
    public static String message_head = "[Chamomile Report]\n已查询到以下数据，下面的数据将按照以下顺序排列\n";

    public ReportQuery() {
        super("query-report");
    }

    public static String query() {
        List<List<String>> data = Report.reportDataManager.readReportFile();
        StringBuilder sb = new StringBuilder();
        sb.append(message_head);

        int index = 0;
        for (List<String> row : data) {
            sb.append(index++).append(" | ");  // 添加序号
            for (String col : row) {
                sb.append(col).append(" | ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }


    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        // 检查发送者是否具有OP权限
        if (!operatorCheck(sender)) {
            try {
                sender.sendMessage(query());
            } catch (Exception e) {
                sender.sendMessage("查询数据失败！");
                LOGGER.warning(e.getMessage());
            }
        }
        return true;
    }
}
