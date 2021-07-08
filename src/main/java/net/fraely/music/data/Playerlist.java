package net.fraely.music.data;

import net.fraely.Side;
import net.fraely.Utils.Musicschedul;
import net.fraely.Utils.Vote;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.*;

import static net.fraely.Main.playerlist;

public class Playerlist {
    private Set<String> playerlist;

    public Playerlist() {
        this.playerlist = new HashSet<>();
    }

    public boolean AddPlayer(Player player) {
        if (!playerlist.contains(player.getName())) {
            playerlist.add(player.getName());
            if (playerlist.size() == 1) {
                Musicschedul.StartMusic();
            }
            return true;
        }
        return false;
    }

    public boolean RemovePlayer(Player player) {
        if (playerlist.contains(player.getName())) {
            playerlist.remove(player.getName());
            Vote.RemoveAgreeNumber(player);
            if (playerlist.size() == 0) {
                Musicschedul.StopMusic();
            }
            return true;
        }
        return false;
    }

    public Set<String> getPlayerlist() {
        return playerlist;
    }

    public void SendToPlayerList(String message) {
        for (String playername : playerlist) {
            Optional<Player> player = Sponge.getServer().getPlayer(playername);
            player.ifPresent(p -> {
                p.sendMessage(Text.builder(message).build());
            });
        }

    }
    public void SendPluginToPlayerList(String message) {
        Side side = new Side();
        for (String playername : playerlist) {
            Optional<Player> player = Sponge.getServer().getPlayer(playername);
            player.ifPresent(p -> {
                side.SendTo(p,message);
            });
        }

    }

    public int Size() {
        return playerlist.size();
    }
}
