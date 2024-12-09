package manager.game.gameplay;

import lombok.Getter;
import lombok.Setter;
import manager.game.team.Team;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class League {
    
    @Setter
    private Map<Team, Integer> teams;

    private Map<Map<String, Team>, Map<String, Integer>> matches;

    private final double prizePool, winnerPrize, secondPrize, thirdPrize, fourthPrize;



    public League(Map<Team, Integer> teams, double prizePool) {
        this.teams = teams;
        this.prizePool = prizePool;
        this.winnerPrize = prizePool * 0.4;
        this.secondPrize = prizePool* 0.3;
        this.thirdPrize = prizePool* 0.2;
        this.fourthPrize = prizePool* 0.1;
        this.matches = new HashMap<>();
        generateMatches(13, 4, 2024);
    }

    public void setPositions(){
        teams = teams.entrySet().stream()
                .sorted((e1, e2) -> {
                    int result = e2.getValue().compareTo(e1.getValue());
                    if (result != 0) return result;

                    result = Integer.compare(e2.getKey().getWins(), e1.getKey().getWins());
                    if (result != 0) return result;

                    result = Integer.compare(e2.getKey().getGoalsBalance(), e1.getKey().getGoalsBalance());
                    if (result != 0) return result;

                    result = Integer.compare(e2.getKey().getGoals(), e1.getKey().getGoals());
                    if (result != 0) return result;

                    while (result == 0) result = Math.random() > 0.5 ? -1 : 1;
                    return result;
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Team[] getWinners(){
        Team[] winners = new Team[4];
        int index = 0;
        for (Team team : teams.keySet()) {
            if (index < 4) {
                winners[index++] = team;
            }
            else break;
        }
        return winners;
    }

    public Team[] getLosers(){
        Team[] losers = new Team[4];
        int index = 0;
        ArrayList<Team> keySet = new ArrayList<>(teams.keySet());
        Collections.reverse(keySet);
        for (Team team : keySet) {
            if (index < 4) {
                losers[index++] = team;
            }
            else break;
        }
        return losers;
    }


    public void generateMatches(int day, int month, int year) {
        int rounds = (this.teams.size() -1);
        boolean passTheWeek = false;
        List<Team> teams = new ArrayList<>(this.teams.keySet());

        for (int round = 1; round <= rounds; round++) {
            Map<String, Team> teamsMap = new HashMap<>();
            Map<String, Integer> dateMap = new HashMap<>();

            for (int i = 0; i < (teams.size()/2); i += 2) {
                Team homeTeam = teams.get(i);
                Team awayTeam = teams.get(i+1);
                teams.remove(homeTeam);
                teams.remove(awayTeam);
                teamsMap.put("home", homeTeam);
                teamsMap.put("away", awayTeam);

                dateMap.put("Day", day);
                dateMap.put("Month", month);
                dateMap.put("Year", year);
            }
            matches.put(teamsMap, dateMap);

            int daysToAdd ;
            if(passTheWeek){
                daysToAdd = 7;
                passTheWeek = false;
                teams = new ArrayList<>(this.teams.keySet());
                Collections.shuffle(teams);
            } else {
                daysToAdd = 1;
                passTheWeek = true;
            }
            LocalDate date = LocalDate.of(year, month, day);
            LocalDate nextDate = date.plusDays(daysToAdd);
            day = nextDate.getDayOfMonth();
            month = nextDate.getMonthValue();
            year = nextDate.getYear();
        }
    }
}



