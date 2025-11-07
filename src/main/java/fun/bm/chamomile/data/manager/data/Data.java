package fun.bm.chamomile.data.manager.data;

import fun.bm.chamomile.data.manager.data.link.LinkData;
import fun.bm.chamomile.data.manager.data.player.PlayerData;

import java.util.List;

/**
 * @author Liycxc
 * Date: 2024/7/17 下午6:01
 */
public class Data {
    public PlayerData playerData;
    public List<LinkData> linkData;
    public Boolean qqChecked = false;
    public Boolean useridChecked = false;
    public long firstJoin = -1;
    public long lastJoin = -1;
    public long lastLogout = -1;
    // move up - start
    // this is for old data and auto migrate from old data
    // also new data will use this to save and then migrated to linkData list
    public long qqNumber;
    public long linkedTime;
    public long userid;
    public long useridLinkedGroup;
    public long useridLinkedTime;
    // move up - end
    public String firstJoinIp = null;
    public String lastJoinIp = null;
}