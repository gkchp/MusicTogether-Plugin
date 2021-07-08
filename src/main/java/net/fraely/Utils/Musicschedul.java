package net.fraely.Utils;

import net.fraely.Side;
import net.fraely.music.Getmusic;
import net.fraely.music.data.Musicdata;
import net.fraely.music.data.Musicplayerlist;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static net.fraely.Main.musicplayerlist;
import static net.fraely.Main.playerlist;

public class Musicschedul {
    private static Task.Builder taskbuilder;
    private static Task task;

    public static boolean StartMusic() {
        if (task == null) {
            Release();
            return true;
        }
        return false;
    }

    public static boolean StopMusic() {
        if (task == null) {
            return false;
        }
        if (task.cancel()) {
            task = null;
            return true;
        }
        return false;
    }

    public static void NextMusic() {
        if (task != null) {
            if (task.cancel()) {
                task = null;
            }
        }
        Release();
    }

    private static void Release() {
        List<Musicdata> musicdata = musicplayerlist.GetMusicdata();
        if (musicplayerlist.Size() != 0) {
            String url = Getmusic.url(musicdata.get(0));
            if (url == null) {
                playerlist.SendToPlayerList(String.format("§b[§6点歌系统§b] §6{§f %s §6}§7 不能播放！可能为VIP歌曲！自动播放下一首！", musicdata.get(0).getName()));
                musicplayerlist.RemoveMusicdata(musicdata.get(0));
                return;
            }
            Side side = new Side();
            for (String playername : playerlist.getPlayerlist()) {
                Optional<Player> player = Sponge.getServer().getPlayer(playername);
                playerlist.SendPluginToPlayerList("[Play]" + url);
            }
            playerlist.SendToPlayerList(String.format("§b[§6点歌系统§b]§7 开始播放歌曲： §6{§f %s §6}", musicdata.get(0).getName()));
            Timer(musicdata.get(0));
        } else playerlist.SendToPlayerList("§b[§6点歌系统§b]§7 当前歌单中没有歌曲 快来点一首吧！");
    }


    public static void Timer(Musicdata musicdata) {
        taskbuilder = Task.builder();
        Long dt = musicdata.getDt() + 7000;//增加7s缓冲延迟
        task = taskbuilder.execute(t -> {
            task = null;
            playerlist.SendToPlayerList(String.format("§b[§6点歌系统§b]§7 歌曲 §6{§f %s §6}§7 已播放完毕。", musicdata.getName()));
            Limit.AddOldMusicList(musicdata);
            musicplayerlist.RemoveMusicdata(musicdata);
        }).delay(dt, TimeUnit.MILLISECONDS).submit(Sponge.getPluginManager().getPlugin("togethermusic").get());
    }
}
