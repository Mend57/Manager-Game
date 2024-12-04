package com.fm.Leagues;

import com.fm.Players.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private final int id;
    private final String name;
    private List<Player> players;
    private Player[] mainPlayers = new Player[11];
    private double salaryBudget;
    private double transactionBudget;
    private double currentSalary;
    private int points;
    private int division;
    private int goals;
    private int wins = 0;
    private int losses = 0;
    private int draws = 0;

    public Team(int id, String name, List<Player> players, double salaryBudget, double transactionBudget, int division) {
        this.id = id;
        this.name = name;
        this.players = players;
        this.salaryBudget = salaryBudget;
        this.transactionBudget = transactionBudget;
        this.division = division;
        setCurrentSalary();
        setGoals();
        setPoints();
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

    public void setSalaryBudget(double salaryBudget) {this.salaryBudget = salaryBudget;}
    public void setTransactionBudget(double transactionBudget) {this.transactionBudget = transactionBudget;}
    public void setPlayers(List<Player> players) {this.players = players;}
    public void setPoints(int points) {this.points = points;}
    public void setDivision(int division) {this.division = division;}
    public void setWins(int wins) {this.wins = wins;}
    public void setLosses(int losses) {this.losses = losses;}
    public void setDraws(int draws) {this.draws = draws;}
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
    public void setMainPlayers() {

    }
    public void setPoints(){

    }

    public double teamCompetence() {
        double competence = 0;
        for (Player player : mainPlayers) {
            competence += player.inGameCompetence();
        }
        return competence;
    }

}
