package net.fraely.Utils;

import net.fraely.music.data.Musicdata;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.fraely.Main.musicplayerlist;
import static net.fraely.Main.playerlist;

public class Limit {
    private static boolean isture = false;
    private static List<Musicdata> oldmusiclist = new ArrayList<>();
    private static Task.Builder taskbuilder;
    private static Task task;

    public static void AddOldMusicList(Musicdata musicdata) {
        if (!oldmusiclist.contains(musicdata)) {
            oldmusiclist.add(musicdata);
        }
    }

    public static void RremoveOldMusicList(Musicdata musicdata) {
        if (oldmusiclist.contains(musicdata)) {
            oldmusiclist.remove(musicdata);
        }
    }

    public static boolean Past(Musicdata lostmusic) {
        if (isture) {
            System.out.println("空闲下一首");
            oldmusiclist.remove(lostmusic);
            int max = oldmusiclist.size() - 1;
            int random = (int) (1 + Math.random() * (max - 0 + 1));
            musicplayerlist.AddMusicdata(oldmusiclist.get(random));
        }
        return isture;
    }

    public static void StopTime() {
        if (task != null) {
            task.cancel();
            isture = false;
        }
    }

    public static void setIsture(boolean is) {
        isture = is;
    }

    public static void Time() {
        taskbuilder = Task.builder();
        task = taskbuilder.execute(t -> {
            task = null;
            if (playerlist.Size() != 0 && oldmusiclist.size() > 15) {
                isture = true;
                playerlist.SendToPlayerList("§b[§6点歌系统§b] §7由于 §f60 §7秒内无人点歌 自动进入空闲模式！");
                int max = oldmusiclist.size() - 1;
                int random = (int) (1 + Math.random() * (max - 0 + 1));
                musicplayerlist.AddMusicdata(oldmusiclist.get(random));
            }
        }).delay(60, TimeUnit.SECONDS).submit(Sponge.getPluginManager().getPlugin("togethermusic").get());
    }
}
