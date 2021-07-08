package net.fraely.Utils;

import net.fraely.music.data.Musicdata;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.fraely.Main.musicplayerlist;
import static net.fraely.Main.playerlist;

public class Vote {
    private static Boolean isture = false;// 投票开始为真反之为假
    private static List<String> agree;//赞成票人数

    private static Task.Builder taskbuilder = Task.builder();
    private static Task task;

    public static String StartVote(Player player) {
        if (playerlist.getPlayerlist().contains(player.getName())) {
            switch (musicplayerlist.Size()) {
                case 0:
                    return "[TogetherMusic.Null]";//当前没有歌曲
                case 1:
                    return "[TogetherMusic.NoNext]";//当前没有下一首歌曲
            }
            if (!isture) {
                isture = true;
                agree = new ArrayList<>();
                playerlist.SendToPlayerList("§b[§6点歌系统§b] §7有人发起了切歌投票 20秒内输入/music agree同意切歌，无视视为反对。");
                Timer();
                return "[TogetherMusic.True]";
            }
            return "[TogetherMusic.Repeat]";//已经开始投票
        }
        return "[TogetherMusic.Empty]";
    }

    public static String AddAgreeNumber(Player player) {
        if (!isture) return "[TogetherMusic.Error]";//没有投票正在进行
        if (playerlist.getPlayerlist().contains(player.getName())) {
            if (!agree.contains(player.getName())) {
                agree.add(player.getName());
                return "[TogetherMusic.True]";
            } else return "[TogetherMusic.Repeat]";//已投过票
        }
        return "[TogetherMusic.Empty]";//不在听歌
    }

    public static void RemoveAgreeNumber(Player player) {
        if (isture) {
            if (agree.contains(player.getName())) {
                agree.remove(player.getName());
            }
        }
    }

    public static void Past() {
        if (isture) {
            task.cancel();
            playerlist.SendToPlayerList("§b[§6点歌系统§b]§7 投票歌曲已播放完毕 投票自动结束！");
        }
    }

    public static void Timer() {
        task = taskbuilder.execute(t -> {
            isture = false;
            task = null;
            int agreesize = agree.size();
            int playerlistsize = playerlist.Size();
            if (agreesize > playerlistsize / 2) {
                playerlist.SendToPlayerList(String.format("§b[§6点歌系统§b] §7切歌投票结束 §6{§f %d / %d §6}§7 同意数过半进入下一首歌曲！", agreesize, playerlistsize));
                Musicschedul.StopMusic();
                musicplayerlist.RemoveMusicdata(musicplayerlist.GetMusicdata().get(0));
            } else
                playerlist.SendToPlayerList(String.format("§b[§6点歌系统§b] §7切歌投票结束 §6{§f %d / %d §6}§7 同意数未过半取消切歌！", agreesize, playerlistsize));
        }).delay(20, TimeUnit.SECONDS).submit(Sponge.getPluginManager().getPlugin("togethermusic").get());
    }
}
