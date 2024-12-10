package manager.game.gameplay;

import lombok.Getter;
import manager.game.team.Team;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Match {

    private final Map<Team, Double> teams = new HashMap<Team, Double>();
    private final int day, month, year;

    public Match(Team home, Team away, int day, int month, int year) {
        double homeMultiplier = 1.1;
        this.teams.put(home, home.teamCompetence() * homeMultiplier + Math.random() * 2);
        this.teams.put(away, away.teamCompetence() + Math.random() * 2);
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public double getPerformance(Team team){
        return teams.get(team);
    }

    public Team getLoser(Team home, Team away) {
        double homePerformance = getPerformance(home);
        double awayPerformance = getPerformance(away);
        if(homePerformance > awayPerformance + 2) return away;
        if(awayPerformance > homePerformance + 2) return home;

        double decider = Math.random();
        Team betterTeam;
        Team worstTeam;
        if(homePerformance >= awayPerformance){
            betterTeam = home;
            worstTeam = away;
        }
        else {
            betterTeam = away;
            worstTeam = home;
        }

        if (decider <= 0.2) return betterTeam;
        if (decider <= 0.5) return worstTeam;
        return null;
    }
}
