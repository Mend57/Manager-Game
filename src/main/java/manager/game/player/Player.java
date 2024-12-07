package manager.game.player;

import manager.game.team.Team;
import manager.game.utils.Value;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Player {
    private final int id;
    private final String name;
    private final int height;
    private final int weight;

    @Setter(AccessLevel.NONE)
    private int agility, passing, impulsion, technique;
    private double price, salary;

    @Setter(AccessLevel.NONE) private int injuryTime;
    @Setter(AccessLevel.NONE) private boolean injury = false;

    private Team currentTeam;

    @Getter(AccessLevel.NONE)
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

    public void addAgility(int agility) {
        this.agility += agility;
    }
    public void addPassing(int passing) {
        this.passing += passing;
    }
    public void addImpulsion(int impulsion) {
        this.impulsion += impulsion;
    }
    public void addTechnique(int technique) {
        this.technique += technique;
    }

    public void enterInjury(){
        this.injury = true;
        injuryTime = injuryGravity();
    }
    public void exitInjury(){
        injury = false;
    }


    protected int jumpReach(){
        return Value.normalize(impulsion + (int)Math.round(0.3 * (20-weight) + 0.7 * height), 40);
    }

    protected void injuryRisk(){
        double injuryChance = Math.random() * 20 + (double)weight / 10;
        if (injuryChance > 20){
            enterInjury();
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


