package fun.bm.command.main.executor.extra.sub.report;

import fun.bm.command.Command;
import fun.bm.data.Report.ReportDataManager;
import org.bukkit.command.CommandSender;

import static fun.bm.util.helper.CommandHelper.checkNotOperator;

public class ReportDelete extends Command.ExecutorE {
    public ReportDelete() {
        super("del-report");
    }

    public boolean executorMain(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (checkNotOperator(sender)) {
            return true;
        }
        if (args.length == 0) {
            ReportQuery rq = new ReportQuery();
            rq.executorMain(sender, command, label, args);
            sender.sendMessage("请输入时间戳");
            return true;
        }
        if (!args[0].matches("\\d+")) {
            ReportQuery rq = new ReportQuery();
            rq.executorMain(sender, command, label, args);
            sender.sendMessage("请输入时间戳");
        } else {
            if (ReportDataManager.deleteData(args[0])) {
                sender.sendMessage("尝试删除成功");
            } else {
                sender.sendMessage("删除失败");
            }
        }
        return true;
    }
}
