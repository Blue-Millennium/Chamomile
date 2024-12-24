package fun.suya.suisuroru.module.impl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Calendar;

import static fun.suya.suisuroru.module.impl.UnionBan.checkBanData;

public class BanListChecker extends JavaPlugin {

    public static void scheduleDailyCheck(JavaPlugin plugin) {
        // 获取当前时间
        Calendar now = Calendar.getInstance();
        // 获取今天0时的时间
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);

        // 如果今天0时已经过去，则设置为明天0时
        if (now.after(midnight)) {
            midnight.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 计算从现在到今天0时的时间差（毫秒）
        long delay = midnight.getTimeInMillis() - now.getTimeInMillis();

        // 调度任务
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            checkBanData();
            // 任务完成后，再次调度下一次任务
            scheduleDailyCheck(plugin);
        }, delay / 50); // 将毫秒转换为tick（1 tick = 50 毫秒）
    }
}
