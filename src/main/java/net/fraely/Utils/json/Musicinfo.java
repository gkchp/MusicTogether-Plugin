package net.fraely.Utils.json;

import java.util.List;

public class Musicinfo {
    private List<Songs> data;
    private int code;

    public String GetName(int key) {
        if (data.isEmpty()) {
            return "[TogetherMusic.Null]";
        }
        return data.get(key).getName();
    }

    public String GetSinger(int key) {
        if (data.isEmpty()) {
            return "[TogetherMusic.Null]";
        }
        return data.get(key).getSinger();
    }

    public Long Gettime(int key) {
        if (data.isEmpty()) {
            return null;
        }
        return data.get(key).getTime();
    }

    public List<Songs> getSongList() {
        return data;
    }

    public int getCode() {
        return code;
    }


    /** public String GetAr(int key) {
     return data.getAr(key);
     }*/
}

