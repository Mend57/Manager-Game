package manager.game.gameplay;

import lombok.Getter;
import lombok.Setter;
import manager.game.myUtils.Value;
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
        this.teams = teams;
        this.matches = new Match[this.teams.size()/2 * (this.teams.size() - 1) * 2];
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
        List<Team> teams = new ArrayList<>(this.teams.keySet()), teamsReversed = new ArrayList<>();
        List<Match> weeklyMatches, allPossibleMatches = allPossibleMatches();
        int rounds = (teams.size() -1), gamesPerWeek = (teams.size()/2), matchesIndex = 0;
        boolean passTheWeek = false;

        for (int round = 1; round <= rounds; round++) {
            do {
                teams = new ArrayList<>(this.teams.keySet());
                Collections.shuffle(teams);
                weeklyMatches = new ArrayList<>();
                removeDays(1);
                for (int i = 0; i < gamesPerWeek * 2; i += 2) {
                    Team homeTeam = teams.get(0);
                    Team awayTeam = teams.get(1);
                    teams.remove(homeTeam);
                    teams.remove(awayTeam);
                    weeklyMatches.add(new Match(homeTeam, awayTeam, day, month, year, Value.concatenateInts(homeTeam.getId(), awayTeam.getId())));
                    if (weeklyMatches.size() == gamesPerWeek / 2) addDays(1);
                }
            } while (isMatchImpossible(allPossibleMatches, weeklyMatches));

            addWeeklyToMatches(weeklyMatches, matchesIndex);
            addWeeklyToReversed(weeklyMatches, teamsReversed);
            removeWeeklyFromPossibleMatches(weeklyMatches, allPossibleMatches);
            matchesIndex += weeklyMatches.size();
            addDays(7);
        }

        for (int round = 1; round <= rounds * 2; round++) {
            for (int i = 0; i < (gamesPerWeek); i += 2) {
                Team homeTeam = teamsReversed.get(0);
                Team awayTeam = teamsReversed.get(1);
                teamsReversed.remove(homeTeam);
                teamsReversed.remove(awayTeam);
                matches[matchesIndex++] = new Match(homeTeam, awayTeam, day, month, year, Value.concatenateInts(homeTeam.getId(), awayTeam.getId()));
            }
            addDays(passTheWeek ? 7 : 1);
            passTheWeek = !passTheWeek;
        }
    }

//    public void generateMatches() {
//        List<Team> teamsList = new ArrayList<>(teams.keySet());
//        int teamCount = teamsList.size();
//        if (teamCount % 2 != 0) {
//            teamsList.add(null); // Adiciona um "bye" se o número de times for ímpar
//        }
//
//        int rounds = teamsList.size() - 1;
//        int gamesPerWeek = teamsList.size() / 2;
//        matches = new Match[rounds * gamesPerWeek * 2]; // Ida e volta
//        int matchIndex = 0;
//
//        for (int round = 0; round < rounds; round++) {
//            List<Match> weeklyMatches = new ArrayList<>();
//
//            // Gera os jogos da semana
//            for (int i = 0; i < gamesPerWeek; i++) {
//                Team homeTeam = teamsList.get(i);
//                Team awayTeam = teamsList.get(teamsList.size() - 1 - i);
//
//                if (homeTeam != null && awayTeam != null) {
//                    weeklyMatches.add(new Match(
//                            homeTeam,
//                            awayTeam,
//                            day,
//                            month,
//                            year,
//                            Value.concatenateInts(homeTeam.getId(), awayTeam.getId())
//                    ));
//                }
//            }
//
//            // Adiciona os jogos de ida ao calendário
//            for (Match match : weeklyMatches) {
//                matches[matchIndex++] = match;
//            }
//
//            // Rotaciona os times para gerar a próxima rodada
//            Team fixed = teamsList.get(0);
//            Team last = teamsList.remove(teamsList.size() - 1);
//            teamsList.add(1, last);
//            teamsList.set(0, fixed);
//
//            addDays(7); // Avança uma semana para os próximos jogos
//        }
//
//        // Gera os jogos de volta
//        for (int round = 0; round < rounds; round++) {
//            for (int i = 0; i < gamesPerWeek; i++) {
//                Team homeTeam = teamsList.get(i);
//                Team awayTeam = teamsList.get(teamsList.size() - 1 - i);
//
//                if (homeTeam != null && awayTeam != null) {
//                    matches[matchIndex++] = new Match(
//                            awayTeam, // Inverte os mandos
//                            homeTeam,
//                            day,
//                            month,
//                            year,
//                            Value.concatenateInts(awayTeam.getId(), homeTeam.getId())
//                    );
//                }
//            }
//
//            // Rotaciona os times para a próxima rodada de volta
//            Team fixed = teamsList.get(0);
//            Team last = teamsList.remove(teamsList.size() - 1);
//            teamsList.add(1, last);
//            teamsList.set(0, fixed);
//
//            addDays(7); // Avança uma semana
//        }
//    }


    private boolean isMatchImpossible(List<Match> possibleMatches, List<Match> weeklyMatches) {
        int possibleCounter = 0;
        for (Match weeklyMatch : weeklyMatches) {
            int weeklyId = weeklyMatch.getId();
            int reversedWeeklyId = Value.reverseNumber(weeklyId);
            for (Match possibleMatch : possibleMatches) {
                if(weeklyId == possibleMatch.getId() || reversedWeeklyId == possibleMatch.getId()){
                    possibleCounter++;
                }
            }
        }
        return possibleCounter != weeklyMatches.size();
    }

    private void addWeeklyToMatches(List<Match> weeklyMatches, int matchesIndex) {
        for (Match weeklyMatch : weeklyMatches) {
            matches[matchesIndex++] = weeklyMatch;
        }
    }

    private void addWeeklyToReversed(List<Match> weeklyMatches , List<Team> teamsReversed) {
        for (Match weeklyMatch : weeklyMatches) {
            teamsReversed.add(weeklyMatch.getAwayTeam());
            teamsReversed.add(weeklyMatch.getHomeTeam());
        }
    }

    private void removeWeeklyFromPossibleMatches(List<Match> weeklyMatches, List<Match> possibleMatches) {
        for (Match weeklyMatch : weeklyMatches) {
            int weeklyId = weeklyMatch.getId();
            int reversedWeeklyId = Value.reverseNumber(weeklyId);
            possibleMatches.removeIf(possibleMatch -> weeklyId == possibleMatch.getId() || reversedWeeklyId == possibleMatch.getId());
        }
        for(Match match : possibleMatches){
            System.out.print(match.getId() + " ");
        }
        System.out.println();
    }

    private List<Match> allPossibleMatches(){
        List<Team> teams = new ArrayList<>(this.teams.keySet());
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Team homeTeam = teams.get(i);
                Team awayTeam = teams.get(j);
                matches.add(new Match(homeTeam, awayTeam, 0, 0,0, Value.concatenateInts(homeTeam.getId(), awayTeam.getId())));
            }
        }
        for(Match match : matches){
            System.out.print(match.getId() + " ");
        }
        System.out.println();
        return matches;
    }

    private void addDays(int daysToAdd){
        LocalDate nextDate = LocalDate.of(year, month, day).plusDays(daysToAdd);
        day = nextDate.getDayOfMonth();
        month = nextDate.getMonthValue();
        year = nextDate.getYear();
    }

    private void removeDays(int daysToRemove){
        LocalDate nextDate = LocalDate.of(year, month, day).minusDays(daysToRemove);
        day = nextDate.getDayOfMonth();
        month = nextDate.getMonthValue();
        year = nextDate.getYear();
    }

}



