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

    private final double prizePool, winnerPrize, secondPrize, thirdPrize, fourthPrize;

    private Match[] matches;

    private int day, month, year;



    public League(Map<Team, Integer> teams, double prizePool, int day, int month, int year) {
        this.teams       = teams;
        this.matches     = new Match[this.teams.size()/2 * (this.teams.size() - 1) * 2];
        this.prizePool   = prizePool;
        this.day         = day;
        this.month       = month;
        this.year        = year;
        this.winnerPrize = prizePool * 0.4;
        this.secondPrize = prizePool* 0.3;
        this.thirdPrize  = prizePool* 0.2;
        this.fourthPrize = prizePool* 0.1;
        generateMatches();
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

    public void generateMatches() {
        int matchIndex = 0;
        List<Team> teamsList = new ArrayList<>(teams.keySet());
        if (teamsList.getFirst().isPlayerCurrentTeam()) Collections.rotate(teamsList, 1);

        generateHalfMatches(teamsList, true, matchIndex);
        matchIndex += matches.length / 2;
        generateHalfMatches(teamsList, false, matchIndex);
    }

    public void generateHalfMatches(List<Team> teamsList, boolean firstHalf ,int matchIndex) {
        if (teamsList.size() % 2 != 0) teamsList.add(null);
        int halfRounds   = teamsList.size() - 1;
        int gamesPerWeek = teamsList.size() / 2;

        for (int round = 0; round < halfRounds; round++) {
            for (int i = 0; i < gamesPerWeek; i++) {
                Team homeTeam = teamsList.get(i);
                Team awayTeam = teamsList.get(teamsList.size() - 1 - i);

                if(i == gamesPerWeek/2) addDays(1);
                if (homeTeam != null && awayTeam != null) {
                    if (firstHalf) matches[matchIndex++] = new Match(homeTeam, awayTeam, day, month, year);
                    else matches[matchIndex++] = new Match(awayTeam, homeTeam, day, month, year);
                }
            }
            teamsList.add(1, teamsList.removeLast());
            teamsList.set(0, teamsList.getFirst());
            addDays(6);
        }
    }

    private void addDays(int daysToAdd){
        LocalDate nextDate = LocalDate.of(year, month, day).plusDays(daysToAdd);
        day   = nextDate.getDayOfMonth();
        month = nextDate.getMonthValue();
        year  = nextDate.getYear();
    }

}



