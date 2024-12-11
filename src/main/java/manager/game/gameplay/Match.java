package manager.game.gameplay;

import lombok.Getter;
import manager.game.team.Team;
import manager.game.myUtils.Value;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Match {

    private final Map<Team, Double> teams = new HashMap<Team, Double>();
    private final Team homeTeam;
    private final Team awayTeam;
    private final int day, month, year, id;

    public Match(Team home, Team away, int day, int month, int year, int id) {
        double homeMultiplier = 1.1;
        this.id = id;
        this.homeTeam = home;
        this.awayTeam = away;
        this.teams.put(home, Value.normalize(home.teamCompetence() * homeMultiplier + Math.random() * 2, Value.getMINIMUM_ATTRIBUTES(),
                Value.getATTRIBUTES_THRESHOLD() * homeMultiplier + 2));
        this.teams.put(away, Value.normalize(away.teamCompetence() + Math.random() * 2, Value.getMINIMUM_ATTRIBUTES(),
                Value.getATTRIBUTES_THRESHOLD() * homeMultiplier + 2));

        this.day = day;
        this.month = month;
        this.year = year;
    }

    public double getPerformance(Team team){
        return teams.get(team);
    }

    public Team getLoser() {
        double homePerformance = getPerformance(homeTeam);
        double awayPerformance = getPerformance(awayTeam);
        if(homePerformance > awayPerformance + 2) return awayTeam;
        if(awayPerformance > homePerformance + 2) return homeTeam;

        double decider = Math.random();
        Team betterTeam;
        Team worstTeam;
        if(homePerformance >= awayPerformance){
            betterTeam = homeTeam;
            worstTeam = awayTeam;
        }
        else {
            betterTeam = awayTeam;
            worstTeam = homeTeam;
        }

        if (decider <= 0.2) return betterTeam;
        if (decider <= 0.5) return worstTeam;
        return null;
    }
}
