package manager.game.gameplay;

import lombok.Getter;
import manager.game.player.Goalkeeper;
import manager.game.player.Outfield;
import manager.game.player.Player;
import manager.game.player.Position;
import manager.game.team.FilterByPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Market implements FilterByPosition {

    @Getter
    static Map<Player, Double[]> playersForSale = new HashMap<>();

    public static void addPlayer(Player player, Double price, Double salary) {
        playersForSale.put(player, new Double[]{price, salary});
        player.setForSale(true);
        player.setPrice(price);
    }
    public static void removePlayer(Player player) {
        playersForSale.remove(player);
        player.setForSale(false);
        player.estimatePrice();
    }

}
