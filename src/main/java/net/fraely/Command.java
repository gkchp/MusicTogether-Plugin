package net.fraely;

import net.fraely.Utils.Getid;
import net.fraely.Utils.Limit;
import net.fraely.Utils.Vote;
import net.fraely.music.Getmusic;
import net.fraely.music.data.Musicdata;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.TextActions;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import static java.lang.Integer.parseInt;
import static net.fraely.Main.musicplayerlist;
import static net.fraely.Main.playerlist;
import static net.fraely.music.data.BlackPlayerlist.blackplayerlist;

public class Command implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource send, CommandContext args) throws CommandException {
        send.sendMessage(Text.builder("§7/music group [歌单ID或链接] ——将歌单的歌曲全部加入点歌列表").build());
        send.sendMessage(Text.builder("§7/music add [歌曲ID或者链接] ——增加一首歌曲").build());
        send.sendMessage(Text.builder("§7/music v [0-130] ——设置音量").build());
        send.sendMessage(Text.builder("§7/music setting [only|stop|start] ——屏蔽本曲|暂停|开始").build());
        send.sendMessage(Text.builder("§7/music list ——显示正在播放和下一首歌曲的名字").build());
        send.sendMessage(Text.builder("§7/music vote ——发起投票切换至下一首歌曲").build());
        send.sendMessage(Text.builder("§7/music agree ——同意切换至下一首歌曲").build());
        send.sendMessage(Text.builder("§7/music search [歌曲名称] ——搜索歌曲").build());

        return CommandResult.success();
    }
}

class CommandList implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource send, CommandContext args) throws CommandException {
        int size = musicplayerlist.Size();
        switch (size) {
            case 1:
                LiteralText build0 = null;
                try {
                    build0 = Text.builder("§d[§9查看歌曲详情§d]").onClick(TextActions.openUrl(new URL("https://music.163.com/song?id=" + musicplayerlist.GetMusicdata().get(0).getUid()))).build();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                send.sendMessage(Text.of(Text.builder(String.format("§b[§6点歌系统§b]§7 当前正在播放： §6{ §f%s ——%s  §b( §7UID: %s §b) §6} ", musicplayerlist.GetMusicdata().get(0).getName(), musicplayerlist.GetMusicdata().get(0).getSinger(), musicplayerlist.GetMusicdata().get(0).getUid())).build(), build0));
                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 下一首歌曲：§6{ §f无 §6}").build());
                break;
            case 0:
                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 当前歌单中没有歌曲 快来点歌吧！").build());
                break;
            default:
                LiteralText buildd0 = null;
                LiteralText build1 = null;
                try {
                    build1 = Text.builder("§d[§9查看歌曲详情§d]").onClick(TextActions.openUrl(new URL("https://music.163.com/song?id=" + musicplayerlist.GetMusicdata().get(1).getUid()))).build();
                    buildd0 = Text.builder("§d[§9查看歌曲详情§d]").onClick(TextActions.openUrl(new URL("https://music.163.com/song?id=" + musicplayerlist.GetMusicdata().get(0).getUid()))).build();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                send.sendMessage(Text.of(Text.builder(String.format("§b[§6点歌系统§b]§7 当前正在播放： §6{ §f%s ——%s  §b( §7UID: %s §b) §6} ", musicplayerlist.GetMusicdata().get(0).getName(), musicplayerlist.GetMusicdata().get(0).getSinger(), musicplayerlist.GetMusicdata().get(0).getUid())).build(), buildd0));
                send.sendMessage(Text.of(Text.builder(String.format("§b[§6点歌系统§b]§7 下一首歌曲： §6{ §f%s ——%s  §b( §7UID: %s §b) §6} ", musicplayerlist.GetMusicdata().get(1).getName(), musicplayerlist.GetMusicdata().get(1).getSinger(), musicplayerlist.GetMusicdata().get(1).getUid())).build(), build1));
        }
        return CommandResult.success();
    }
}

class CommandV implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource send, CommandContext args) throws CommandException {
        if (send instanceof Player) {
            if (blackplayerlist.contains(send.getName())) {
                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你未安装Mod！").build());
                return CommandResult.success();
            }
            String message = args.<String>getOne("size").get();
            try {
                parseInt(message);
            } catch (NumberFormatException e) {
                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 请输入范围 §6{§f 0-130 §6} 的数字").build());
            }
            new Side().SendTo((Player) send, "[v]" + message);
            send.sendMessage(Text.builder(String.format("§b[§6点歌系统§b]§7 音量已调整为：§f %s%%", message)).build());
        } else send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你无法使用此命令！").build());
        return CommandResult.success();
    }
}

class CommandAdd implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource send, CommandContext args) throws CommandException {
        if (send instanceof Player) {
            if (playerlist.getPlayerlist().contains(send.getName())) {
                Long uid = 0L;
                try {
                    uid = Getid.GetId(args.<String>getOne("uid|url").get());
                } catch (URISyntaxException e) {
                    if (e.toString().equals("[TogetherMusic.ErrorKey]"))
                        send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 输入格式错误 请输入正确的UID或URL！").build());
                    return CommandResult.success();
                }
                Musicdata musicdata = Getmusic.info(uid);
                switch (musicdata.getName()) {
                    case "[TogetherMusic.Null]":
                        send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 无法查询到歌曲请确认后重新输入！").build());
                        break;
                    default:
                        Limit.StopTime();
                        if (musicplayerlist.Size() == 0) {
                            send.sendMessage(Text.builder(String.format("§b[§6点歌系统§b]§7 成功点歌 §6{§f %s §6}", musicdata.getName())).build());
                            musicplayerlist.AddMusicdata(musicdata);
                        } else {
                            if (musicplayerlist.AddMusicdata(musicdata)) {
                                send.sendMessage(Text.builder(String.format("§b[§6点歌系统§b]§7 歌曲§6 { §f%s §6}§7 加入成功！", musicdata.getName())).build());
                            } else {
                                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 播放列表中已经有这首歌曲了！").build());
                            }
                        }
                        break;
                }
            } else
                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 错误 你早已退出点歌 输入§6 {§f /music set start §6} §7重新加入。").build());
        } else send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你无法使用此命令！").build());
        return CommandResult.success();
    }
}

class CommandGroup implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource send, CommandContext args) throws CommandException {
        if (send instanceof Player) {
            if (playerlist.getPlayerlist().contains(send.getName())) {
                String id = args.<String>getOne("uid|url").get();
                try {
                    Long uid = 0L;
                    uid = Getid.GetId(args.<String>getOne("uid|url").get());
                    List<Musicdata> musiclist = Getmusic.Listinfo(uid);
                    if (musiclist == null) {
                        send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 无法查询到歌单 或 歌单中无歌曲！").build());
                        return CommandResult.success();
                    }
                    int musiclistsize = musiclist.size();
                    if (musicplayerlist.Size() == 0) {
                        send.sendMessage(Text.builder(String.format("§b[§6点歌系统§b]§7 成功点歌 §6{§f %s §6}", musiclist.get(0).getName())).build());
                        send.sendMessage(Text.builder(String.format("§b[§6点歌系统§b]§7 其余 §6{§f %d §6} §7首歌曲已添加至歌单中。", musiclistsize - 1)).build());
                    } else {
                        send.sendMessage(Text.builder(String.format("§b[§6点歌系统§b]§7 歌曲 §6{§f %s §6} §7等 §6{§f %d §6}§7 首歌曲已去重加入歌单", musiclist.get(0).getName(), musiclistsize)).build());
                    }
                    Limit.StopTime();
                    for (int i = 0; i < musiclistsize; i++) {
                        musicplayerlist.AddMusicdata(musiclist.get(i));
                    }
                } catch (URISyntaxException e) {
                    send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 输入格式错误 请输入正确的UID或URL！").build());
                }
            } else
                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 错误 你早已退出点歌 输入§6 {§f /music set start §6} §7重新加入。").build());
        } else send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你无法使用此命令！").build());
        return CommandResult.success();
    }
}

class CommandSetting implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource send, CommandContext args) throws CommandException {
        if (send instanceof Player) {
            if (blackplayerlist.contains(send.getName())) {
                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你未安装Mod！").build());
                return CommandResult.success();
            }
            Player player = (Player) send;
            send.sendMessage(Text.builder("§7/music setting only ——屏蔽本曲").build());
            send.sendMessage(Text.builder("§7/music setting start ——加入点歌").build());
            send.sendMessage(Text.builder("§7/music setting stop ——退出点歌").build());
        } else send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你无法使用此命令！").build());
        return CommandResult.success();
    }
}

class CommandSettingonly implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource send, CommandContext args) throws CommandException {
        if (send instanceof Player) {
            if (blackplayerlist.contains(send.getName())) {
                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你未安装Mod！").build());
                return CommandResult.success();
            }
            new Side().SendTo((Player) send, "[Stop]");
            send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你已跳过当前播放的歌曲。").build());

        } else send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你无法使用此命令！").build());
        return CommandResult.success();
    }
}

class CommandSettingstart implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource send, CommandContext args) throws CommandException {
        if (send instanceof Player) {
            if (blackplayerlist.contains(send.getName())) {
                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你未安装Mod！").build());
                return CommandResult.success();
            }
            if (playerlist.AddPlayer((Player) send))
                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 歌曲将在下一首开始时响起。").build());
            else send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 错误 你现在正在听歌 如无声音请检查音量！").build());
        } else send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你无法使用此命令！").build());
        return CommandResult.success();
    }
}

class CommandSettingstop implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource send, CommandContext args) throws CommandException {
        if (send instanceof Player) {
            if (blackplayerlist.contains(send.getName())) {
                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你未安装Mod！").build());
                return CommandResult.success();
            }
            if (playerlist.RemovePlayer((Player) send)) {
                new Side().SendTo((Player) send, "[Stop]");
                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你已不再参与点歌 输入 §6{§7 /music set start §6} §7重新加入。").build());
            } else send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 错误 你已经退出过点歌！").build());
        } else send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你无法使用此命令！").build());
        return CommandResult.success();
    }
}


class CommandVote implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource send, CommandContext args) throws CommandException {
        if (send instanceof Player) {
            switch (Vote.StartVote((Player) send)) {
                case "[TogetherMusic.True]":
                    send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你已成功发起切歌并默认同意！").build());
                    Vote.AddAgreeNumber((Player) send);
                    break;
                case "[TogetherMusic.Repeat]":
                    send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 已经有人发起了切歌输入§6 {§f /music agree §6} §7同意切歌。").build());
                    break;
                case "[TogetherMusic.Empty]":
                    send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你早已退出点歌 输入§6 {§f /music set start §6} §7重新加入。").build());
                    break;
                case "[TogetherMusic.Null]":
                    send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 当前没有歌曲正在播放！").build());
                    break;
                case "[TogetherMusic.NoNext]":
                    send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 没有下一首歌可以播放！").build());
                    break;
            }
        } else send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你无法使用此命令！").build());
        return CommandResult.success();
    }
}

class CommandAgree implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource send, CommandContext args) throws CommandException {
        if (send instanceof Player) {
            switch (Vote.AddAgreeNumber((Player) send)) {
                case "[TogetherMusic.True]":
                    send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 投票成功！").build());
                    break;
                case "[TogetherMusic.Error]":
                    send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 没有正在进行的投票 输入§6 {§f /music vote §6} §7开始歌曲切换投票！").build());
                    break;
                case "[TogetherMusic.Repeat]":
                    send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你已经投过了 请勿重复投票！").build());
                    break;
                case "[TogetherMusic.Empty]":
                    send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你早已退出点歌 输入§6 {§f /music set start §6} §7重新加入。").build());
                    break;
            }
        } else send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你无法使用此命令！").build());
        return CommandResult.success();
    }
}

class CommandSearch implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource send, CommandContext args) throws CommandException {
        if (send instanceof Player) {
            if (playerlist.getPlayerlist().contains(send.getName())) {
                String search = args.<String>getOne("Search").get();
                List<Musicdata> listinfo = null;
                try {
                    listinfo = Getmusic.Searchinfo(URLEncoder.encode(search, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (listinfo != null) {
                    send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 只显示搜索到的前十首歌，点击歌名打开歌曲详情页。").build());
                    for (int i = 0; i < listinfo.size(); i++) {
                        Musicdata musicdata = listinfo.get(i);
                        ClickAction.SuggestCommand suggestCommand = TextActions.suggestCommand(String.format("/music add %s", musicdata.getUid()));
                        LiteralText builder = Text.builder("§d[§9点歌§d]").onClick(suggestCommand).build();
                        LiteralText musicname;
                        try {
                            musicname = Text.builder(musicdata.getName()).onClick(TextActions.openUrl(new URL("https://music.163.com/song?id=" + musicdata.getUid()))).build();
                            send.sendMessage(Text.of(Text.builder("§6{ §f").build(), musicname, Text.builder(String.format(" ——%s §b(§7UID: %s§b) §6} ", musicdata.getSinger(), musicdata.getUid())).build(), builder));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                    send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 输入§6 {§f /music add [UID] §6} §7来点歌。").build());
                } else send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 未搜索到歌曲。").build());
            } else
                send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 错误 你早已退出点歌 输入§6 {§f /music set start §6} §7重新加入。").build());
        } else send.sendMessage(Text.builder("§b[§6点歌系统§b]§7 你无法使用此命令！").build());
        return CommandResult.success();
    }
}