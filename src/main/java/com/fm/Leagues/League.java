package com.fm.Leagues;

import com.fm.Players.Player;
import lombok.Getter;
import lombok.Setter;

public class League {
    @Getter @Setter
    private Team[] teams;

    @Getter
    private final double prizePool, winnerPrize, secondPrize, thirdPrize;

    League(Team[] teams, double prizePool) {
        this.teams = teams;
        this.prizePool = prizePool;
        this.winnerPrize = prizePool/2;
        this.secondPrize = winnerPrize*0.6;
        this.thirdPrize = winnerPrize*0.4;
    }
}


