package net.fraely;

import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fraely.music.data.BlackPlayerlist;
import net.fraely.music.data.Musicplayerlist;
import net.fraely.music.data.Playerlist;
import org.slf4j.Logger;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.network.ChannelRegistrar;
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.nio.charset.StandardCharsets;

@Plugin(
        id = "togethermusic",
        name = "TogetherMusic",
        version = "1.0.1"
)
public class Main {

    public static Musicplayerlist musicplayerlist = new Musicplayerlist();
    public static Playerlist playerlist = new Playerlist();

    @Inject
    private Logger logger;

    public Main() {
    }

    @Listener
    public void onServerStart(GameInitializationEvent event) {
        ChannelRegistrar channelRegistrar = Sponge.getChannelRegistrar();//注册通讯
        new Side().rawChannel = channelRegistrar.createRawChannel(this, "allmusic:channel");//开启频道
        Side.rawChannel.addListener(Platform.Type.SERVER, (data, connection, side) -> {
            ByteBuf buf = Unpooled.wrappedBuffer((ByteBuf) data);
            String message = buf.toString(StandardCharsets.UTF_8);
            if (message.contains("[Check]") || message.contains("666")) {
                PlayerConnection a = (PlayerConnection) connection;
                boolean remove = BlackPlayerlist.blackplayerlist.remove(a.getPlayer().getName());
                if (remove) {
                    playerlist.AddPlayer(a.getPlayer());
                    a.getPlayer().sendMessage(Text.builder("§b[§6点歌系统§b]§7 Mod已验证！").build());
                }
            }
        });
        CommandSpec musicsettingstart = CommandSpec
                .builder()
                .permission("together.music.setting.start")
                .executor(new CommandSettingstart())
                .description(Text.of("§b[§6点歌系统§b]§7 设置start指令"))
                .build();
        CommandSpec musicsettingstop = CommandSpec
                .builder()
                .permission("together.music.setting.stop")
                .executor(new CommandSettingstop())
                .description(Text.of("§b[§6点歌系统§b]§7 设置stop指令"))
                .build();
        CommandSpec musicsettingonly = CommandSpec
                .builder()
                .permission("together.music.setting.only")
                .executor(new CommandSettingonly())
                .description(Text.of("§b[§6点歌系统§b]§7 设置only指令"))
                .build();
        CommandSpec musicgrounp = CommandSpec
                .builder()
                .permission("together.music.group")
                .executor(new CommandGroup())
                .description(Text.of("§b[§6点歌系统§b]§7 搜索歌曲名称"))
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("uid|url")))
                .build();
        CommandSpec musicsearch = CommandSpec
                .builder()
                .permission("together.music.search")
                .executor(new CommandSearch())
                .description(Text.of("§b[§6点歌系统§b]§7 搜索歌曲名称"))
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("Search")))
                .build();
        CommandSpec musicagree = CommandSpec
                .builder()
                .permission("together.music.agree")
                .executor(new CommandAgree())
                .description(Text.of("§b[§6点歌系统§b]§7 同意切换至下一首歌曲"))
                .arguments(GenericArguments.none())
                .build();
        CommandSpec musicvote = CommandSpec
                .builder()
                .permission("together.music.vote")
                .executor(new CommandVote())
                .description(Text.of("§b[§6点歌系统§b]§7 发起投票切换至下一首歌曲"))
                .arguments(GenericArguments.none())
                .build();
        CommandSpec musiclist = CommandSpec
                .builder()
                .permission("together.music.list")
                .executor(new CommandList())
                .description(Text.of("§b[§6点歌系统§b]§7 查看当前点歌歌单"))
                .arguments(GenericArguments.none())
                .build();
        CommandSpec musicv = CommandSpec
                .builder()
                .permission("together.music.v")
                .description(Text.of("§b[§6点歌系统§b]§7 调整音量"))
                .arguments(GenericArguments.string(Text.of("size")))
                .executor(new CommandV())
                .build();
        CommandSpec musicadd = CommandSpec
                .builder()
                .permission("together.music.add")
                .description(Text.of("§b[§6点歌系统§b]§7 增加一首歌曲"))
                .arguments(GenericArguments.string(Text.of("uid|url")))
                .executor(new CommandAdd())
                .build();
        CommandSpec musicsetting = CommandSpec
                .builder()
                .permission("together.music.setting")
                .description(Text.of("§b[§6点歌系统§b]§7 退出或者加入点歌"))
                .arguments(GenericArguments.none())
                .child(musicsettingonly,"only")
                .child(musicsettingstart,"start")
                .child(musicsettingstop,"stop")
                .executor(new CommandSetting())
                .build();
        CommandSpec command = CommandSpec
                .builder()
                .permission("together.music")
                .arguments(GenericArguments.none())
                .executor(new Command())
                .description(Text.of("§b[§6点歌系统§b]§7 父命令以及帮助"))
                .child(musiclist, "list", "l")
                .child(musicv, "v")
                .child(musicadd, "add")
                .child(musicsetting, "setting", "set")
                .child(musicvote, "vote")
                .child(musicagree, "agree")
                .child(musicsearch, "search")
                .child(musicgrounp, "group")
                .build();
        Sponge.getCommandManager().register(this, command, new String[]{"music"});//注册主命令
        Sponge.getEventManager().registerListeners(this, new Event());//注册事件
        System.out.println("[点歌系统] TogetherMusic 加载完毕！");
    }
}
