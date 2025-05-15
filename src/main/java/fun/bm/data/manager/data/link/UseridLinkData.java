package fun.bm.data.manager.data.link;

public class UseridLinkData extends LinkData {
    public long userid;
    public long useridLinkedGroup;

    public UseridLinkData(long id, long group, long time) {
        this.userid = id;
        this.useridLinkedGroup = group;
        this.linkedTime = time;
    }
}
