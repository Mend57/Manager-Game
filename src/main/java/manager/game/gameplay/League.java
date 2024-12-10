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

    private final Match[] matches;

    private int day, month, year;



    public League(Map<Team, Integer> teams, double prizePool, int day, int month, int year) {
        this.teams = teams;
        this.matches = new Match[this.teams.size()/2 * ((this.teams.size() - 1) * 2)];
        this.prizePool = prizePool;
        this.day = day;
        this.month = month;
        this.year = year;
        this.winnerPrize = prizePool * 0.4;
        this.secondPrize = prizePool* 0.3;
        this.thirdPrize = prizePool* 0.2;
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
        int rounds = (this.teams.size() -1);
        boolean passTheWeek = false;
        List<Team> teams = new ArrayList<>(this.teams.keySet());
        List<Team> teamsReversed = new ArrayList<>();
        int matchesIndex = 0;

        for (int round = 1; round <= rounds * 2; round++) {
            for (int i = 0; i < (teams.size()/2); i += 2) {
                Team homeTeam = teams.get(0);
                Team awayTeam = teams.get(1);
                teams.remove(homeTeam);
                teams.remove(awayTeam);
                teamsReversed.add(awayTeam);
                teamsReversed.add(homeTeam);
                matches[matchesIndex++] = new Match(homeTeam, awayTeam, day, month, year);
            }

            int daysToAdd = passTheWeek ? 7 : 1;
            if(passTheWeek){
                teams = new ArrayList<>(this.teams.keySet());
                Collections.shuffle(teams);
            }
            passTheWeek = !passTheWeek;
            addDays(daysToAdd);
        }

        for (int round = 1; round <= rounds * 2; round++) {
            for (int i = 0; i < (teams.size()/2); i += 2) {
                Team homeTeam = teamsReversed.get(0);
                Team awayTeam = teamsReversed.get(1);
                teamsReversed.remove(homeTeam);
                teamsReversed.remove(awayTeam);
                matches[matchesIndex++] = new Match(homeTeam, awayTeam, day, month, year);
            }

            int daysToAdd = passTheWeek ? 7 : 1;
            passTheWeek = !passTheWeek;
            addDays(daysToAdd);
        }
    }

    private void addDays(int daysToAdd){
        LocalDate nextDate = LocalDate.of(year, month, day).plusDays(daysToAdd);
        day = nextDate.getDayOfMonth();
        month = nextDate.getMonthValue();
        year = nextDate.getYear();
    }
}



