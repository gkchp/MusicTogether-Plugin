package net.fraely;

import net.fraely.music.data.BlackPlayerlist;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import static net.fraely.Main.playerlist;

public class Event {
    @Listener
    public void PlayerJoin(ClientConnectionEvent.Join event) {
        new Side().SendTo(event.getTargetEntity(),"[Check]");
        BlackPlayerlist.blackplayerlist.add(event.getTargetEntity().getName());
    }
    @Listener
    public void PlayerLeave(ClientConnectionEvent.Disconnect event) {
        playerlist.RemovePlayer(event.getTargetEntity());
        BlackPlayerlist.blackplayerlist.remove(event.getTargetEntity().getName());
    }
}
