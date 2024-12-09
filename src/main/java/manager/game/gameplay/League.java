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
        generateMatches(15, 4, 2024, 4);
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


    public void generateMatches(int startDay, int startMonth, int startYear, int gamesPerDay) {
        int numRounds = teams.size() - 1;

        LocalDate currentDate = LocalDate.of(startYear, startMonth, startDay);

        for (int round = 1; round <= numRounds * 2; round++) {
            Map<String, Team> teamsMap = new HashMap<>();
            Map<String, Integer> dateMap = new HashMap<>();

            int gamesToday = 0;
            if (Math.random() > 0.7) {
                gamesToday = (int) (Math.random() * 3) + 1;
            }

            for (int j = 0; j < gamesToday; j++) {
                Team homeTeam = teams.keySet().toArray(new Team[0])[j];
                Team awayTeam = teams.keySet().toArray(new Team[0])[teams.size() - 1 - j];
                teamsMap.put("home", homeTeam);
                teamsMap.put("away", awayTeam);

                dateMap.put("Day", currentDate.getDayOfMonth());
                dateMap.put("Month", currentDate.getMonthValue());
                dateMap.put("Year", currentDate.getYear());
            }
            matches.put(teamsMap, dateMap);

            currentDate = currentDate.plusDays(1);
            if (currentDate.getDayOfMonth() > currentDate.lengthOfMonth()) {
                currentDate = currentDate.withDayOfMonth(1);
                currentDate = currentDate.plusMonths(1);
            }
            if(currentDate.getMonthValue() > currentDate.lengthOfYear()) {
                currentDate = currentDate.withDayOfMonth(1);
                currentDate = currentDate.withMonth(1);
                currentDate = currentDate.plusYears(1);
            }
        }
    }
}



