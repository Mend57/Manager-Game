package com.fm.Leagues;

import com.fm.Players.Player;

import java.util.*;

public class Team {
    private final int id;
    private final String name;
    private List<Player> players;
    private Player[] mainPlayers = new Player[11];
    private double salaryBudget;
    private double transactionBudget;
    private double currentSalary;
    private int division;
    private int goals;
    private int goalsAgainst;
    private int totalGoals;
    private int wins = 0;
    private int losses = 0;
    private int draws = 0;
    private int points;
    private Tactic tactic;

    public Team(int id, String name, List<Player> players, double salaryBudget, double transactionBudget, int division, Tactic tactic) {
        setCurrentSalary();
        setGoals();
        setPoints();
        setGoalsAgainst(0);
        setTotalGoals();
        this.id = id;
        this.name = name;
        this.players = players;
        this.salaryBudget = salaryBudget;
        this.transactionBudget = transactionBudget;
        this.division = division;
        this.tactic = tactic;

    }

    public int getId() {return id;}
    public String getName() {return name;}public List<Player> getPlayers() {return players;}
    public double getSalaryBudget() {return salaryBudget;}
    public double getTransactionBudget() {return transactionBudget;}
    public double getTotalBudget() {return salaryBudget + transactionBudget;}
    public double getCurrentSalary() {return currentSalary;}
    public int getPoints() {return points;}
    public int getDivision() {return division;}
    public int getGoals() {return goals;}
    public int getWins() {return wins;}
    public int getLosses() {return losses;}
    public int getDraws() {return draws;}
    public int getTotalGoals() {return totalGoals;}
    public Tactic getTactic() {return tactic;}

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
    public void setTotalGoals() {this.totalGoals = goals - goalsAgainst;}
    public void setCurrentSalary() {
        for (Player player : players) {
            currentSalary += player.getSalary();
        }
    }
    public void setGoals() {
        for (Player player : players) {
            goals += player.getGoals();
        }
    }
    public void setMainPlayers(Tactic currentTactic) {
        int[] defensePlayer = new int[currentTactic.tactic[0]];
        int[] midfieldPlayers = new int[currentTactic.tactic[1]];
        int[] attackPlayers = new int[currentTactic.tactic[2]];
        Map<Integer, Double> competenceMap = new HashMap<>();


        for (Player player : players) {
            competenceMap.put(player.getId(), player.competence());
        }
        List<Map.Entry<Integer, Double>> competenceList = new ArrayList<>(competenceMap.entrySet());
        competenceList.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));



    }
    public void setGoalsAgainst(int goals) {
        goalsAgainst += goals;
    }

    public double teamCompetence() {
        double competence = 0;
        for (Player player : mainPlayers) {
            competence += player.inGameCompetence();
        }
        return competence;
    }

}
