package manager.game.gameplay;

import manager.game.player.Goalkeeper;
import manager.game.player.Outfield;
import manager.game.player.Player;
import manager.game.player.Position;
import manager.game.team.FilterByPosition;

import java.util.ArrayList;
import java.util.List;

public class Market implements FilterByPosition {

    List<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        players.add(player);
    }
    public void removePlayer(Player player) {
        players.remove(player);
    }

}
