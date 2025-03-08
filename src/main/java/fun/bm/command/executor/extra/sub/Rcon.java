package fun.bm.command.executor.extra.sub;

import fun.bm.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static fun.bm.util.RconCommandExecute.executeRconCommand;

public class Rcon extends Command.ExecutorE {
    public Rcon() {
        super("rcon");
    }

    public boolean executorMain(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage("此命令为测试命令，不显示使用方法");
        String address = args[0];
        String[] parts = address.split(":");
        String IP;
        int port;
        if (parts.length == 2) {
            IP = parts[0];
            port = Integer.parseInt(parts[1]);
        } else {
            sender.sendMessage("请输入正确的IP地址和端口号");
            return true;
        }
        String psd = args[1];
        StringBuilder CommandBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            CommandBuilder.append(args[i]).append(" ");
        }
        String full_command = CommandBuilder.toString().trim();
        sender.sendMessage(executeRconCommand(IP, port, psd, full_command));
        return true;
    }
}
