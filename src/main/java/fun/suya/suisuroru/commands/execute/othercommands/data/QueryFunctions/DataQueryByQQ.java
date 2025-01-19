package fun.suya.suisuroru.commands.execute.othercommands.data.QueryFunctions;

import fun.suya.suisuroru.data.AuthData.DataGet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.suya.suisuroru.commands.execute.othercommands.data.QueryFunctions.DataQueryByUUID.ProcessFinalData;
import static fun.xd.suka.Main.LOGGER;

public class DataQueryByQQ implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("您没有权限这么做");
            return true;
        }
        DataGet dataGet = new DataGet();
        long QQNum = 0;
        try {
            QQNum = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§c输入的数据不是数字");
            return true;
        } catch (Exception e) {
            LOGGER.info(String.valueOf(e));
            return true;
        }
        String playerJson = dataGet.getPlayersByQQAsJson(QQNum);
        return ProcessFinalData(sender, playerJson);
    }
}
