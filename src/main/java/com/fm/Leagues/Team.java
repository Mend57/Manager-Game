package com.fm.Leagues;

import com.fm.Players.Goalkeeper;
import com.fm.Players.Outfield;
import com.fm.Players.Player;
import com.fm.Players.Position;

import java.util.*;
import java.util.stream.Collectors;

public class Team {
    private final int id;
    private final String name;
    private List<Player> players, mainSquad;
    private Player[] mainPlayers, reservePlayers;
    private Tactic tactic;

    private double salaryBudget, transactionBudget;
    private double salaryCost;

    private int division, goals, goalsAgainst, totalGoals, wins = 0, losses = 0, draws = 0, points;

    public Team(int id, String name, List<Player> players, double salaryBudget, double transactionBudget, int division, Tactic tactic) {
        setGoals();
        setPoints();
        setGoalsAgainst(0);
        setTotalGoals();
        this.id = id;
        this.name = name;
        this.players = players;
        this.salaryBudget = salaryBudget;
        setSalaryCost();
        this.transactionBudget = transactionBudget;
        this.division = division;
        this.tactic = tactic;
        autoMainSquad(tactic);
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public double getSalaryBudget() {return salaryBudget;}
    public double getTransactionBudget() {return transactionBudget;}
    public double getTotalBudget() {return salaryBudget + transactionBudget;}
    public double getCurrentSalary() {return salaryCost;}
    public int getPoints() {return points;}
    public int getDivision() {return division;}
    public int getGoals() {return goals;}
    public int getWins() {return wins;}
    public int getLosses() {return losses;}
    public int getDraws() {return draws;}
    public int getTotalGoals() {return totalGoals;}
    public Tactic getTactic() {return tactic;}
    public List<Player> getPlayers() {return players;}
    public Player[] getMainPlayers() {return mainPlayers;}
    public Player[] getReservePlayers() {return reservePlayers;}
    public List<Player> getMainSquad() {return mainSquad;}
    public List<Goalkeeper> getGoalkeepers() {
        List<Goalkeeper> goalkeepers = new ArrayList<>();
        for (Player player : players) {
            if (player instanceof Goalkeeper) {
                goalkeepers.add((Goalkeeper) player);
            }
        }
        return goalkeepers;
    }
    public List<Outfield> getOutfielders() {
        List<Outfield> outfielders = new ArrayList<>();
        for (Player player : players) {
            if (player instanceof Outfield) {
                outfielders.add((Outfield)player);
            }
        }
        return outfielders;
    }
    public List<Outfield> getPlayersInPosition(Position position) {
        List<Outfield> playersInPosition = new ArrayList<>();
        for (Outfield player : getOutfielders()) {
            if (player.getPosition().equals(position)) {
                playersInPosition.add(player);
            }
        }
        return playersInPosition;
    }
    


    public void setSalaryBudget(double salaryBudget) {this.salaryBudget = salaryBudget;}
    public void setTransactionBudget(double transactionBudget) {this.transactionBudget = transactionBudget;}
    public void setPlayers(List<Player> players) {this.players = players;}
    public void setPoints(int points) {this.points = points;}
    public void setDivision(int division) {this.division = division;}
    public void setWins(int wins) {this.wins = wins;}
    public void setLosses(int losses) {this.losses = losses;}
    public void setDraws(int draws) {this.draws = draws;}
    public void setTactic(Tactic tactic) {this.tactic = tactic;}
    public void setPoints(){points = wins * 3 + draws;}
    public void setGoalsAgainst(int goals) {
        goalsAgainst += goals;
    }
    public void setTotalGoals() {this.totalGoals = goals - goalsAgainst;}
    public void setSalaryCost() {
        for (Player player : players) {
            salaryCost += player.getSalary();
        }
    }
    public void setGoals() {
        for (Player player : players) {
            goals += player.getGoals();
        }
    }
    
    

    public void autoMainSquad(Tactic currentTactic) {
        int expectedDefensorNumber = currentTactic.tactic[0], expectedMidfieldersNumber = currentTactic.tactic[1], expectedAttackersNumber = currentTactic.tactic[2];
        List<Outfield> defensors = getPlayersInPosition(Position.DEFENSE), midfielders= getPlayersInPosition(Position.MIDFIELD), attackers= getPlayersInPosition(Position.ATTACK);

        Player[] mainDefensePlayers = new Player[expectedDefensorNumber], mainMidfieldPlayers = new Player[expectedMidfieldersNumber], mainAttackPlayers = new Player[expectedAttackersNumber],
                 reserveDefensePlayers = new Player[2], reserveMidfieldPlayers = new Player[2], reserveAttackPlayers = new Player[2];

        Map<Player, Double> outfieldersCompetenceMap = new HashMap<>();
        Map<Player, Double> defensorsCompetenceMap = new HashMap<>();
        Map<Player, Double> attackersCompetenceMap = new HashMap<>();
        Map<Player, Double> midfieldersCompetenceMap = new HashMap<>();
        Map<Player, Double> goalkeepersCompetenceMap = new HashMap<>();

        for (Player player : getGoalkeepers()) goalkeepersCompetenceMap.put(player, player.competence());
        for (Player player : getOutfielders()) outfieldersCompetenceMap.put(player, player.competence());
        for (Player player : defensors) defensorsCompetenceMap.put(player, player.competence());
        for (Player player : midfielders) midfieldersCompetenceMap.put(player, player.competence());
        for (Player player : attackers) attackersCompetenceMap.put(player, player.competence());

        Map<Player, Double> goalkeepersSorted = sortPlayersByCompetence(goalkeepersCompetenceMap);
        Map<Player, Double> outfieldersSorted = sortPlayersByCompetence(outfieldersCompetenceMap);
        Map<Player, Double> defensorsSorted = sortPlayersByCompetence(defensorsCompetenceMap);
        Map<Player, Double> midfieldersSorted = sortPlayersByCompetence(midfieldersCompetenceMap);
        Map<Player, Double> attackersSorted = sortPlayersByCompetence(attackersCompetenceMap);

        boolean hasGoalKeeper = false;
        for(Map.Entry<Player, Double> entry : goalkeepersSorted.entrySet()) {
            if (!hasGoalKeeper) {
                mainPlayers[0] = entry.getKey();
                goalkeepersSorted.remove(entry.getKey());
                hasGoalKeeper = true;
            }
            else {
                reservePlayers[0] = entry.getKey();
                goalkeepersSorted.remove(entry.getKey());
                break;
            }
        }

        int defenseIndex = 0, midfieldIndex = 0, attackIndex = 0;
        for (Map.Entry<Player, Double> entry : defensorsSorted.entrySet()) {
            Outfield player = (Outfield) entry.getKey();
            if (defenseIndex < expectedDefensorNumber) {
                mainDefensePlayers[defenseIndex++] = player;
                defensorsSorted.remove(player);
                outfieldersSorted.remove(player);
                player.setCurrentPosition(Position.DEFENSE);
            } else break;
        }
        for (Map.Entry<Player, Double> entry : midfieldersSorted.entrySet()) {
            Outfield player = (Outfield) entry.getKey();
            if (midfieldIndex < expectedMidfieldersNumber) {
                mainMidfieldPlayers[midfieldIndex++] = player;
                midfieldersSorted.remove(player);
                outfieldersSorted.remove(player);
                player.setCurrentPosition(Position.MIDFIELD);
            } else break;
        }
        for (Map.Entry<Player, Double> entry : attackersSorted.entrySet()) {
            Outfield player = (Outfield) entry.getKey();
            if (attackIndex < expectedAttackersNumber) {
                mainAttackPlayers[attackIndex++] = player;
                attackersSorted.remove(player);
                outfieldersSorted.remove(player);
                player.setCurrentPosition(Position.ATTACK);
            } else break;
        }
        concatenateMainPlayers(mainDefensePlayers, mainMidfieldPlayers, mainAttackPlayers);

        for (int i = 1; i < mainPlayers.length; i++ ) {
            if (!outfieldersSorted.isEmpty()) {
                if (mainPlayers[i] == null) {
                    Map.Entry<Player, Double> firstEntryMidfield = midfieldersSorted.entrySet().iterator().next();
                    Map.Entry<Player, Double> firstEntryOutfield = outfieldersSorted.entrySet().iterator().next();
                    Outfield playerMidfield = (Outfield) firstEntryMidfield.getKey();
                    Outfield player = (Outfield) firstEntryOutfield.getKey();
                    if (midfieldersSorted.isEmpty()) {
                        mainPlayers[i] = player;
                        outfieldersSorted.remove(player);
                        if (i > expectedDefensorNumber) player.setCurrentPosition(Position.ATTACK);
                        else player.setCurrentPosition(Position.DEFENSE);

                        if(player.getPosition() == Position.DEFENSE) defensorsSorted.remove(player);
                        else attackersSorted.remove(player);

                    } else {
                        mainPlayers[i] = playerMidfield;
                        outfieldersSorted.remove(playerMidfield);
                        midfieldersSorted.remove(playerMidfield);
                        playerMidfield.setCurrentPosition(Position.MIDFIELD);
                    }
                }
            } else break;
        }

        int reserveDefenseIndex = 0, reserveMidfieldIndex = 0, reserveAttackIndex = 0;
        for (Map.Entry<Player, Double> entry : defensorsSorted.entrySet()) {
            Outfield player = (Outfield) entry.getKey();
            if (reserveDefenseIndex < reserveDefensePlayers.length) {
                reserveDefensePlayers[reserveDefenseIndex++] = player;
                defensorsSorted.remove(player);
                outfieldersSorted.remove(player);
            } else break;
        }
        for (Map.Entry<Player, Double> entry : midfieldersSorted.entrySet()) {
            Outfield player = (Outfield) entry.getKey();
            if (reserveMidfieldIndex < reserveMidfieldPlayers.length) {
                reserveMidfieldPlayers[reserveMidfieldIndex++] = player;
                midfieldersSorted.remove(player);
                outfieldersSorted.remove(player);
            } else break;
        }
        for (Map.Entry<Player, Double> entry : attackersSorted.entrySet()) {
            Outfield player = (Outfield) entry.getKey();
            if (reserveAttackIndex < reserveAttackPlayers.length) {
                reserveAttackPlayers[reserveAttackIndex++] = player;
                attackersSorted.remove(player);
                outfieldersSorted.remove(player);
            } else break;
        }
        concatenateReservePlayers(reserveDefensePlayers, reserveMidfieldPlayers, reserveAttackPlayers);

        for (int i = 1; i < reservePlayers.length; i++ ) {
            if (!goalkeepersSorted.isEmpty() || !outfieldersSorted.isEmpty()) {
                if(reservePlayers[i] == null) {
                    Map.Entry<Player, Double> firstEntryOutfield = outfieldersSorted.entrySet().iterator().next();
                    Outfield player = (Outfield) firstEntryOutfield.getKey();
                    Map.Entry<Player, Double> firstEntryGoalkeeper = goalkeepersSorted.entrySet().iterator().next();
                    Goalkeeper goalkeeper = (Goalkeeper) firstEntryGoalkeeper.getKey();
                    if (!outfieldersSorted.isEmpty()) {
                        reservePlayers[i] = player;
                        outfieldersSorted.remove(player);
                        switch (player.getPosition()){
                            case DEFENSE:
                                defensorsSorted.remove(player);
                                break;
                            case MIDFIELD:
                                midfieldersSorted.remove(player);
                                break;
                            case ATTACK:
                                attackersSorted.remove(player);
                                break;
                        }

                    }
                    else{
                        reservePlayers[i] = goalkeeper;
                        goalkeepersSorted.remove(goalkeeper);
                    }
                }
            } else break;
        }
    }


    private Map<Player, Double> sortPlayersByCompetence(Map<Player, Double> competenceMap){
        return competenceMap.entrySet().stream()
                .sorted(Map.Entry.<Player, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private void concatenateMainPlayers(Player[] defensePlayers, Player[] midfieldPlayers, Player[] attackPlayers) {
        for (int i = 1; i < mainPlayers.length; i++){
            if(i <= defensePlayers.length){
                mainPlayers[i] = defensePlayers[i - 1];
            }
            else if(i <= defensePlayers.length + midfieldPlayers.length){
                mainPlayers[i] = midfieldPlayers[i - 1 - defensePlayers.length];
            }
            else if(i <= defensePlayers.length + midfieldPlayers.length + attackPlayers.length){
                mainPlayers[i] = attackPlayers[i - 1 - defensePlayers.length - midfieldPlayers.length];

            }
        }
    }

    private void concatenateReservePlayers(Player[] defensePlayers, Player[] midfieldPlayers, Player[] attackPlayers) {
        for (int i = 1; i < reservePlayers.length; i++){
            if(i <= defensePlayers.length){
                reservePlayers[i] = defensePlayers[i - 1];
            }
            else if(i <= defensePlayers.length + midfieldPlayers.length){
                reservePlayers[i] = midfieldPlayers[i - 1 - defensePlayers.length];
            }
            else if(i <= defensePlayers.length + midfieldPlayers.length + attackPlayers.length){
                reservePlayers[i] = attackPlayers[i - 1 - defensePlayers.length - midfieldPlayers.length];

            }
        }
    }

    public int tacticalFamiliarity() {

    }

    public double teamCompetence() {
        double mainCompetence = 0;
        double reserveCompetence = 0;
        for (Player player : mainPlayers) {
            mainCompetence += player.inGameCompetence();
        }
        for (Player player : reservePlayers) {
            reserveCompetence += player.inGameCompetence();
        }
        return mainCompetence + reserveCompetence/2;
    }
}
