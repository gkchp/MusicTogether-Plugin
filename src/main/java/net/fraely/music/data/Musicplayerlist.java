package net.fraely.music.data;

import net.fraely.Main;
import net.fraely.Side;
import net.fraely.Utils.Limit;
import net.fraely.Utils.Musicschedul;
import net.fraely.Utils.Vote;

import java.util.ArrayList;
import java.util.List;

import static net.fraely.Main.playerlist;

public class Musicplayerlist {
    private List<Musicdata> musicdatalist;

    public Musicplayerlist() {
        this.musicdatalist = new ArrayList<>();
    }

    public boolean AddMusicdata(Musicdata musicdata) {
        if (!musicdatalist.contains(musicdata)) {
            musicdatalist.add(musicdata);
            if (musicdatalist.size() == 1 && Main.playerlist.Size() != 0) {
                Musicschedul.StartMusic();
            }
            return true;
        }
        return false;
    }

    public boolean RemoveMusicdata(Musicdata musicdata) {
        if (musicdatalist.contains(musicdata)) {
            musicdatalist.remove(musicdata);
            Vote.Past();
            if (musicdatalist.size() == 0) {
                if (!Limit.Past(musicdata)) {//返回true表示已经空闲无需新的计时器
                    Limit.Time();
                    Musicschedul.StopMusic();
                    playerlist.SendPluginToPlayerList("[Stop]");
                    playerlist.SendToPlayerList("§b[§6点歌系统§b]§7 已经没有下一首歌了 快来点一首吧！");
                }
            } else {
                if (playerlist.Size() != 0) Musicschedul.NextMusic();
                else Musicschedul.StopMusic();
            }
            return true;
        }
        return false;
    }

    public List<Musicdata> GetMusicdata() {
        return musicdatalist;
    }

    public int Size() {
        return musicdatalist.size();
    }
}
