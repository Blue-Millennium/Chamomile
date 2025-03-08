package fun.bm.command.executor.extra.sub.report;

import fun.bm.command.manager.model.ExecutorE;
import fun.bm.data.Report.ReportDataActions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static fun.bm.Chamomile.LOGGER;
import static fun.bm.util.CommandOperatorCheck.checkNotOperator;

/**
 * @author Suisuroru
 * Date: 2024/9/29 22:08
 * function: Query report data
 */
public class ReportQuery extends ExecutorE {

    public ReportQuery() {
        super("query-report");
    }

    public static String query() {
        List<List<String>> data = ReportDataActions.ReadReportFile();
        StringBuilder sb = new StringBuilder();
        sb.append("[Chamomile Report]\n已查询到以下数据，下面的数据将按照以下顺序排列\n");

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


    public boolean executorMain(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 检查发送者是否具有OP权限
        if (checkNotOperator(sender)) {
        } else {

            try {
                String QueryData = query();
                sender.sendMessage(QueryData);
            } catch (Exception e) {
                sender.sendMessage("查询数据失败！");
                LOGGER.warning(e.getMessage());
            }
        }
        return true;
    }
}
