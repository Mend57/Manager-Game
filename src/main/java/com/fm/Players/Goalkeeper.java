package com.fm.Players;

import com.fm.Leagues.Team;
import com.fm.Utils.Value;

import java.util.List;

public class Goalkeeper extends Player{

    public Goalkeeper(int id, String name, int height, int weight, int agility, int passing, int technique,
                      int impulsion, double price, double salary, Team currentTeam) {

        super(id, name, height, weight, agility, passing, impulsion, technique, price, salary, currentTeam);

    }

    @Override
    public double competence(){
        return Value.normalize(jumpReach() + getAgility()+getPassing()+getTechnique(), 80.0);
    }

    @Override
    public double inGameCompetence(){
        return Math.random() * 3 + competence();
    }

    @Override
    protected void injuryRisk() {
        if (canGetInjured()){
            super.injuryRisk();
        }
    }

    private boolean canGetInjured(){
        List<Goalkeeper> goalkeepers = getCurrentTeam().getGoalkeepers();
        return goalkeepers.size() > 1 && injuredGoalkeepers() < goalkeepers.size() - 1;
    }

    private int injuredGoalkeepers(){
        int injuredGoalkeepers = 0;
        List<Goalkeeper> goalkeepers = getCurrentTeam().getGoalkeepers();
        for(Goalkeeper goalkeeper : goalkeepers){
            if (goalkeeper.isInjury()){
                injuredGoalkeepers++;
            }
        }
        return injuredGoalkeepers;
    }
}
