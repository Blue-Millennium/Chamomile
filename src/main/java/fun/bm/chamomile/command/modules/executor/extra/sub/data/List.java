package fun.bm.chamomile.command.modules.executor.extra.sub.data;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.data.manager.data.Data;
import fun.bm.chamomile.util.Environment;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static fun.bm.chamomile.data.processor.data.DataStringBuilder.buildDataString;
import static fun.bm.chamomile.util.Environment.LOGGER;
import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

public class List extends Command.ExecutorE {
    public List() {
        super(null);
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (operatorCheck(sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§e请输入查询数据的头尾，查询全部请输入-1");
            sender.sendMessage("§e例如查询低1-10个数据，请输入1:10");
        } else {
            int start, end;
            try {
                start = Integer.parseInt(args[0].split(":")[0]);
                end = -1;
                if (start != -1) end = Integer.parseInt(args[0].split(":")[1]);
                if (start > end) {
                    int temp = start;
                    start = end;
                    end = temp;
                } else if (start < -1) throw new IllegalArgumentException();
            } catch (Exception e) {
                sender.sendMessage("§c请输入正确的格式");
                sender.sendMessage("§e请输入查询数据的头尾，查询全部请输入-1");
                sender.sendMessage("§e例如查询第1-10个数据，请输入1:10");
                return true;
            }
            try {
                if (start == -1) {
                    buildDataString(sender, Environment.dataManager.DATA_LIST);
                } else {
                    start--;
                    end--;
                    java.util.List<Data> data = new ArrayList<>();
                    for (int i = start; i <= end; i++) data.add(Environment.dataManager.DATA_LIST.get(i));
                    buildDataString(sender, data);
                }
            } catch (Exception e) {
                sender.sendMessage("§c出现内部错误");
                LOGGER.warning(e.getMessage());
            }
        }
        return true;
    }
}
