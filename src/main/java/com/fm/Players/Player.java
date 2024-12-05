package com.fm.Players;

import com.fm.Leagues.Team;
import com.fm.Utils.Value;

public abstract class Player {
    private final int id;
    private final String name;
    private final int height;
    private final int weight;

    private int agility, passing, impulsion, technique;
    private double price, salary;

    private boolean injury = false;
    private int injuryTime;
    private int goals = 0;
    private Team currentTeam;

    private final int minHeight = 150, maxHeight = 200, minWeight = 50, maxWeight = 90;




    public Player(int id, String name, int height, int weight, int agility, int passing, int impulsion,
                  int technique, double price, double salary, Team currentTeam) {

        this.id = id;
        this.name = name;
        this.height = Value.normalize(height - minHeight, maxHeight-minHeight);
        this.weight = Value.normalize(weight - minWeight, maxWeight-minWeight);
        this.agility = agility;
        this.passing = passing;
        this.impulsion = impulsion;
        this.technique = technique;
        this.price = price;
        this.salary = salary;
        this.currentTeam = currentTeam;
    }

    public abstract double inGameCompetence();
    public abstract double competence();

    public int getId() {return id;}
    public String getName() {return name;}
    public int getHeight() {return height;}
    public int getWeight() {return weight;}
    public int getAgility() {return agility;}
    public int getPassing() {return passing;}
    public int getImpulsion() {return impulsion;}
    public int getTechnique() {return technique;}
    public int getInjuryTime() {return injuryTime;}
    public boolean getInjury(){return injury;}
    public double getPrice() {return price;}
    public double getSalary(){return salary;}
    public Team getCurrentTeam() {return currentTeam;}
    public int getGoals() {return goals;}

    public void setSalary(double salary){this.salary = salary;}
    public void setAgility(int agility){this.agility = agility;}
    public void setPassing(int passing){this.passing = passing;}
    public void setImpulsion(int impulsion){this.impulsion = impulsion;}
    public void setTechnique(int technique){this.technique = technique;}
    public void setPrice(double price){this.price = price;}
    public void setCurrentTeam(Team currentTeam){this.currentTeam = currentTeam;}
    public void setGoals(int goals){this.goals = goals;}
    public void setInjury(boolean injury, int time){
        this.injury = injury;
        this.injuryTime = time;
    }


    protected int jumpReach(){
        return Value.normalize(impulsion + (int)Math.round(0.3 * (20-weight) + 0.7 * height), 40);
    }

    protected void injuryRisk(){
        double injuryChance = Math.random() * 20 + (double)weight / 10;
        if (injuryChance > 20){
            setInjury(true, injuryGravity());
        }
    }

    protected int injuryGravity(){
        double gravity = Math.random() * 20;
        if(gravity <= 13){
            return (int)Math.round(1 + Math.random() * 4);
        }
        else if(gravity <= 16){
            return (int)Math.round(7 + Math.random() * 7);
        }
        else if(gravity <= 18){
            return (int)Math.round(7 + Math.random() * 14);
        }
        else return (int)Math.round(28 + Math.random() * 28);
    }

}


