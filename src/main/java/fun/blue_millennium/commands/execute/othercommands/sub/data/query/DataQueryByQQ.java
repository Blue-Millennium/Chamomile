package fun.blue_millennium.commands.execute.othercommands.sub.data.query;

import fun.blue_millennium.data.AuthData.DataGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.blue_millennium.Chamomile.LOGGER;
import static fun.blue_millennium.data.AuthData.DataProcess.ProcessFinalData;
import static fun.blue_millennium.util.CommandOperatorCheck.checkNotOperator;

public class DataQueryByQQ implements CommandExecutor {

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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (checkNotOperator(sender)) {
            return true;
        }
        DataGet dataGet = new DataGet();
        long QQNum = LongProcess(sender, args);
        if (QQNum == 0) {
            return true;
        }
        String playerJson = dataGet.getPlayersByQQAsJson(QQNum);
        return ProcessFinalData(sender, playerJson);
    }
}
