package net.fraely.music.data;

import java.net.URL;
import java.util.Objects;

public class Musicdata {
    private String name;//歌名
    private String uid;//歌曲UID
    private Long dt;//歌曲时长（毫秒）
    private String singer;//作者
    private URL url;//暂时无用 歌曲播放/下载地址

    public Musicdata(String name,String singer, Long dt, String uid) {
        this.name = name;
        this.singer = singer;
        this.dt = dt;
        this.uid = uid;
    }

    public String getSinger() {
        return singer;
    }

    public URL getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public Long getDt() {
        return dt;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Musicdata)) return false;
        Musicdata musicdata = (Musicdata) o;
        return getName().equals(musicdata.getName()) &&
                getUid().equals(musicdata.getUid()) &&
                getDt().equals(musicdata.getDt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUid(), getDt());
    }
}
