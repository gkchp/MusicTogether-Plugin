package net.fraely;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.network.ChannelBinding;

import java.nio.charset.StandardCharsets;

public class Side {
    public static ChannelBinding.RawDataChannel rawChannel;

    public void SendTo(Player player, String mes) {
        byte[] bytes = mes.getBytes(StandardCharsets.UTF_8);
        rawChannel.sendTo(player, channelBuf -> {
            channelBuf.writeByteArray(bytes);
        });
    }
    public void SendToAll(String mes) {
        byte[] bytes = mes.getBytes(StandardCharsets.UTF_8);
        rawChannel.sendToAll(channelBuf -> {
            channelBuf.writeByteArray(bytes);
        });
    }

}
