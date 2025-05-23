package fun.bm.command.modules.executor.extra.sub.report;

import fun.bm.command.Command;
import org.bukkit.command.CommandSender;

import static fun.bm.util.helper.CommandHelper.operatorCheck;

public class ReportDelete extends Command.ExecutorE {
    public ReportDelete() {
        super("del-report");
    }

    public boolean executorMain(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!operatorCheck(sender)) {
            if (args.length == 0 || !args[0].matches("\\d+")) {
                ReportQuery rq = new ReportQuery();
                rq.executorMain(sender, command, label, args);
                sender.sendMessage("§4请输入时间戳");
            } else {
                if (Report.reportDataManager.deleteData(args[0])) {
                    sender.sendMessage("§c尝试删除成功");
                } else {
                    sender.sendMessage("§4删除失败");
                }
            }
        }
        return true;
    }
}
