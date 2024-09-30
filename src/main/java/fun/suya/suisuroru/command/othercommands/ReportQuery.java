package fun.suya.suisuroru.command.othercommands;

import fun.suya.suisuroru.data.ReportDataRead;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Suisuroru
 * Date: 2024/9/29 22:08
 * function: Query report data
 */
public class ReportQuery implements CommandExecutor {

    public static String query() {
        List<List<String>> data = ReportDataRead.ReadReportFile();
        StringBuilder sb = new StringBuilder();
        sb.append("已查询到以下数据，下面的数据将按照以下顺序排列\n");

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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 检查发送者是否具有OP权限
        if (!sender.isOp()) {
            sender.sendMessage("你没有权限执行此命令！");
            return true;
        }

        try {
            String QueryData = query();
            // Replace all matching characters (§x)
            String regex = "§.";
            sender.sendMessage(QueryData.replaceAll(regex, ""));
            return true;
        } catch (Exception e) {
            sender.sendMessage("查询数据失败！" + e.getMessage());
            return false;
        }
    }
}
