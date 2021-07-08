package net.fraely.Utils.json;

public class Songs {
    private String name;
    private String id;
    private String singer;
    private long time;
    //private List<ar> ar;

    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public long getTime() {
        return time*1000;
    }

    public String getUid() {
        return id;
    }
}
