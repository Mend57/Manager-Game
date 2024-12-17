package manager.game.team;

import manager.game.player.Goalkeeper;
import manager.game.player.Outfield;
import manager.game.player.Player;
import manager.game.player.Position;

import java.util.ArrayList;
import java.util.List;

public interface FilterByPosition {

    default List<Outfield> getPlayersInPosition(Position position, List<Outfield> outfielders) {
        List<Outfield> playersInPosition = new ArrayList<>();
        for (Outfield player : outfielders) {
            if (player.getPosition().equals(position)) {
                playersInPosition.add(player);
            }
        }
        return playersInPosition;
    }

    default List<Outfield> getOutfielders(List<Player> players) {
        List<Outfield> outfielders = new ArrayList<>();
        for (Player player : players) {
            if (player instanceof Outfield && !player.isInjury()) {
                outfielders.add((Outfield) player);
            }
        }
        return outfielders;
    }

    default List<Goalkeeper> getGoalkeepers(List<Player> players) {
        List<Goalkeeper> goalkeepers = new ArrayList<>();
        for (Player player : players) {
            if (player instanceof Goalkeeper && !player.isInjury()) {
                goalkeepers.add((Goalkeeper) player);
            }
        }
        return goalkeepers;
    }
}
