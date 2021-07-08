package net.fraely.Utils.json;

import java.util.List;

public class MusicUrl {
    private List<obj> data;
    private int code;//识别码

    public String getUrl() {
        if (data.get(0).getUrl().isEmpty()) {
            return null;
        }
        return data.get(0).getUrl();
    }

    public int getCode() {
        return code;
    }
}

class obj {
    private String url;//播放/下载地址


    public String getUrl() {
        return url;
    }


}
