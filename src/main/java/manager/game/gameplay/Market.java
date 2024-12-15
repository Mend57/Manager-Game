package manager.game.gameplay;

import manager.game.player.Goalkeeper;
import manager.game.player.Outfield;
import manager.game.player.Player;
import manager.game.player.Position;

import java.util.ArrayList;
import java.util.List;

public class Market {

    List<Player> players = new ArrayList<>();

    public List<Outfield> getPlayersInPosition(Position position) {
        List<Outfield> playersInPosition = new ArrayList<>();
        for (Outfield player : getOutfielders()) {
            if (player.getPosition().equals(position)) {
                playersInPosition.add(player);
            }
        }
        return playersInPosition;
    }

    private List<Outfield> getOutfielders() {
        List<Outfield> outfielders = new ArrayList<>();
        for (Player player : players) {
            if (player instanceof Outfield && !player.isInjury()) {
                outfielders.add((Outfield) player);
            }
        }
        return outfielders;
    }

    public List<Goalkeeper> getGoalkeepers() {
        List<Goalkeeper> goalkeepers = new ArrayList<>();
        for (Player player : players) {
            if (player instanceof Goalkeeper && !player.isInjury()) {
                goalkeepers.add((Goalkeeper) player);
            }
        }
        return goalkeepers;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}
