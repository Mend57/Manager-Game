package manager.game.player;

import manager.game.team.Team;
import manager.game.utils.Value;

import java.util.List;

public class Goalkeeper extends Player{

    public Goalkeeper(int id, String name, int height, int weight, int agility, int passing, int technique,
                      int impulsion, double price, double salary, Team currentTeam) {

        super(id, name, height, weight, agility, passing, impulsion, technique, price, salary, currentTeam);

    }

    @Override
    public double competence(){
        return Value.normalize(jumpReach() + getAgility() + getPassing() + getTechnique(), 4 * Value.getMINIMUM_ATTRIBUTES(), 4 * Value.getATTRIBUTES_THRESHOLD());
    }

    @Override
    public double inGameCompetence(){
        return Value.normalize(Math.random() * 3 + competence(), Value.getMINIMUM_ATTRIBUTES(), 3 + Value.getATTRIBUTES_THRESHOLD());
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
