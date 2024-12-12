package manager.game.main;

import manager.game.gameplay.League;
import manager.game.gameplay.Match;
import manager.game.player.Goalkeeper;
import manager.game.player.Outfield;
import manager.game.player.Player;
import manager.game.player.Position;
import manager.game.team.Formation;
import manager.game.team.Team;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ManagerGameApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerGameApplication.class, args);

        List<Player> players = new ArrayList<Player>();
        List<Outfield> outfielders = new ArrayList<Outfield>();
        List<Goalkeeper> goalkeepers = new ArrayList<Goalkeeper>();
        List<Team> teams =  new ArrayList<Team>();

        for (int i = 0; i < 400; i++){
            Outfield outfielder = new Outfield(i, "Player " + i, 175, 60,
                    randomizeNumber(1, 20), randomizeNumber(1, 20), randomizeNumber(1, 20),
                    randomizeNumber(1, 20), randomizeNumber(1, 20), randomizeNumber(1, 20),
                    randomizeNumber(1, 20), randomizeNumber(1, 20), randomizeNumber(1, 20),
                    randomizeNumber(1, 20), randomizePosition(), 1000, 10000,
                    null);
            players.add(outfielder);
            outfielders.add(outfielder);
        }
        for (int i = 0; i < 60; i++){
            Goalkeeper goalkeeper = new Goalkeeper(i, "Goalkeeper " + i, 185,
                    70, randomizeNumber(1, 20), randomizeNumber(1, 20),
                    randomizeNumber(1, 20), randomizeNumber(1, 20), 1000, 10000, null);
            players.add(goalkeeper);
            goalkeepers.add(goalkeeper);
        }

        for (int i = 0; i < 20; i++){
            List<Outfield> outfielder = new ArrayList<Outfield>();
            List<Goalkeeper> goalkeeper = new ArrayList<Goalkeeper>();
            List<Player> player = new ArrayList<Player>();
            for (int j = 0; j < 20; j++){
                outfielder.add(outfielders.get(0));
                outfielders.remove(outfielders.get(0));
            }
            for (int j = 0; j < 3; j++){
                goalkeeper.add(goalkeepers.getFirst());
                goalkeepers.remove(goalkeepers.getFirst());
            }
            player.addAll(outfielder);
            player.addAll(goalkeeper);
            Team team = new Team(i, "Team " + i, player, 10000000,
                    100000, 1);
            teams.add(team);
        }

        Map<Team, Integer> teamMap = new HashMap<>();
        teams.forEach(team -> teamMap.put(team, team.getPoints()));

        League league1 = new League(teamMap, 100000, 1, 2, 2024);
    }
//        System.out.println(league1.getMatches()[0].getHomeTeam().getName() + " " + league1.getMatches()[0].getAwayTeam().getName());
//        System.out.println(league1.getMatches()[190].getHomeTeam().getName() + " " + league1.getMatches()[190].getAwayTeam().getName());
//        System.out.println();
//
//        for(Match match : league1.getMatches()){
//            System.out.println(match.getHomeTeam().getName() + " " + match.getAwayTeam().getName() + " : " + match.getDay() + " / " + match.getMonth() + " / " + match.getYear());
//        }

    private static int randomizeNumber(int min, int max){
        return min + (int)Math.round(Math.random() * (max - min));
    }

    private static Position randomizePosition(){
        double randomNum = Math.random();
        if(randomNum < 0.33){
            return Position.ATTACK;
        }
        if(randomNum < 0.66){
            return Position.DEFENSE;
        }
        else return Position.MIDFIELD;
    }

}
