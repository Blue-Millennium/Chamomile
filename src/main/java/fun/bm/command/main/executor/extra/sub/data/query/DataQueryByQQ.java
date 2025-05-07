package fun.bm.command.main.executor.extra.sub.data.query;

import fun.bm.command.Command;
import fun.bm.data.DataProcessor.Data.DataGet;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.data.DataProcessor.Data.DataStringBuilder.buildDataString;
import static fun.bm.util.MainEnv.LOGGER;
import static fun.bm.util.helper.CommandHelper.checkNotOperator;

public class DataQueryByQQ extends Command.ExecutorE {

    public DataQueryByQQ() {
        super(null);
    }

    public static long LongProcess(CommandSender sender, String[] args) {
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
        if (checkNotOperator(sender)) {
            return true;
        }
        DataGet dataGet = new DataGet();
        long QQNum = LongProcess(sender, args);
        if (QQNum == 0) {
            return true;
        }
        String playerJson = dataGet.getPlayersByQQAsJson(QQNum);
        return buildDataString(sender, playerJson);
    }
}
