package manager.game.gameplay;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import manager.game.team.Team;

@Getter @Setter
public class League {
    private Team[] teams;

    @Setter(AccessLevel.NONE)
    private final double prizePool, winnerPrize, secondPrize, thirdPrize;

    League(Team[] teams, double prizePool) {
        this.teams = teams;
        this.prizePool = prizePool;
        this.winnerPrize = prizePool/2;
        this.secondPrize = winnerPrize*0.6;
        this.thirdPrize = winnerPrize*0.4;
    }
}


