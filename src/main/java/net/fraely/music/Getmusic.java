package net.fraely.music;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import net.fraely.Utils.json.MusicUrl;
import net.fraely.Utils.json.Musicinfo;
import net.fraely.Utils.json.Musiclist;
import net.fraely.Utils.json.Songs;
import net.fraely.music.data.Musicdata;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Integer.parseInt;
import static net.fraely.Utils.Http.getjson;

public class Getmusic {
    public static Musicdata info(Long uid) {
        String urljson = getjson("https://v1.itooi.cn/netease/song?format=1&id=" + uid);
        Gson gson = new Gson();
        Musicinfo musicinfo = gson.fromJson(urljson, Musicinfo.class);
        return new Musicdata(musicinfo.GetName(0), musicinfo.GetSinger(0), musicinfo.Gettime(0), uid+"");
    }

    public static List<Musicdata> Listinfo(Long uid) {
        String urljson = getjson("https://v1.itooi.cn/netease/songList?format=1&id=" + uid);
        Gson gson = new Gson();
        Musiclist musiclistinfo = gson.fromJson(urljson, Musiclist.class);
        if (musiclistinfo.getCode() == 500 || musiclistinfo.getMusicList().isEmpty()) return null;
        List<Songs> songlist = musiclistinfo.getMusicList();
        List<Musicdata> musiclist = new ArrayList<>();
        for (Songs song : songlist) {
            musiclist.add(new Musicdata(song.getName(), song.getSinger(), song.getTime(), song.getUid()));
        }
        return musiclist;
    }

    public static List<Musicdata> Searchinfo(String uid) {
        String urljson = getjson("https://v1.itooi.cn/netease/search?format=1&type=song&pageSize=10&keyword=" + uid);
        Gson gson = new Gson();
        Musicinfo musicinfo = gson.fromJson(urljson, Musicinfo.class);
        if (musicinfo.getCode() == 500) return null;
        List<Musicdata> musicdatalist = new ArrayList<>();
        for (Songs song : musicinfo.getSongList()) {
            musicdatalist.add(new Musicdata(song.getName(), song.getSinger(), song.getTime(), song.getUid()));
        }
        return musicdatalist;
    }

    public static String url(Musicdata musicdata) {
        if (musicdata.getUid() != null) {
            String urljson = getjson("https://api.imjad.cn/cloudmusic/?type=song&br=198000&id=" + musicdata.getUid());
            Gson gson = new Gson();
            MusicUrl musicurl = gson.fromJson(urljson, MusicUrl.class);
            if (musicurl.getUrl() == null) return null;
            return musicurl.getUrl();
        }
        return null;
    }
}
