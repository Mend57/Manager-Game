package com.fm.Leagues;

public class Serie {
    private Team[] teams;
    private final double prizePool;
    private final double winnerPrize;
    private final double secondPrize;
    private final double thirdPrize;

    Serie(Team[] teams, double prizePool) {
        this.teams = teams;
        this.prizePool = prizePool;
        this.winnerPrize = prizePool/2;
        this.secondPrize = winnerPrize*0.6;
        this.thirdPrize = winnerPrize*0.4;
    }

    public void setTeams(Team[] teams) {this.teams = teams;}

    public Team[] getTeams() { return teams; }
    public double getPrizePool(){return prizePool;}
    public double getWinnerPrize() {return winnerPrize;}
    public double getSecondPrize() {return secondPrize;}
    public double getThirdPrize() {return thirdPrize;}
}


