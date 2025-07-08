package fun.bm.chamomile.data.manager.data.link;

public class QQLinkData extends LinkData {
    public long qqNumber;

    public QQLinkData(long qq, long time) {
        this.qqNumber = qq;
        this.linkedTime = time;
    }
}
