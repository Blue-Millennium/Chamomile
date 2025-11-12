package fun.bm.chamomile.command.modules.extra.sub.report;

import fun.bm.chamomile.command.ExtraCommand;
import fun.bm.chamomile.util.Environment;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

public class ReportDelete extends ExtraCommand implements CommandExecutor {
    public ReportDelete() {
        super("del-report");
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!operatorCheck(sender)) {
            if (args.length == 0 || !args[0].matches("\\d+")) {
                ReportQuery.query(sender);
                sender.sendMessage("§4请输入时间戳");
            } else {
                if (Environment.dataManager.reportDataManager.deleteData(args[0])) {
                    sender.sendMessage("§c尝试删除成功");
                } else {
                    sender.sendMessage("§4删除失败");
                }
            }
        }
        return true;
    }
}
