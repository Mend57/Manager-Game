package com.fm.Players;

import com.fm.Utils.Value;

public class Goalkeeper extends Player{

    public Goalkeeper(int id, String name, int height, int weight, int agility, int passing, int technique,
                      int impulsion, double price, double salary, int currentTeam) {

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
}
