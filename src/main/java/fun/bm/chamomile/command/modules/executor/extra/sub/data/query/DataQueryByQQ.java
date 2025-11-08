package fun.bm.chamomile.command.modules.executor.extra.sub.data.query;

import fun.bm.chamomile.command.Command;
import fun.bm.chamomile.command.modules.executor.extra.sub.data.Query;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.chamomile.util.Environment.LOGGER;
import static fun.bm.chamomile.util.data.DataStringBuilder.buildDataString;
import static fun.bm.chamomile.util.helper.CommandHelper.operatorCheck;

public class DataQueryByQQ extends Command.ExecutorE {
    public DataQueryByQQ() {
        super(null);
    }

    public static long longProcess(CommandSender sender, String[] args) {
        long long_num;
        try {
            long_num = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§c输入的数据不是数字");
            return 0;
        } catch (Exception e) {
            LOGGER.info(String.valueOf(e));
            return 0;
        }
        return long_num;
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (operatorCheck(sender)) {
            return true;
        }
        long QQNum = longProcess(sender, args);
        if (QQNum == 0) {
            return true;
        }
        return buildDataString(sender, Query.dataGet.getPlayersByQQ(QQNum));
    }
}
