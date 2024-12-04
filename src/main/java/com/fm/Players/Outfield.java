package com.fm.Players;

import com.fm.Utils.Value;

public class Outfield extends Player {
    private int finishing;
    private int marking;
    private int dribbling;
    private int longShots;
    private int velocity;
    private int stamina;
    private final Position position;
    private Position currentPosition;


    public Outfield(int id, String name, int height, int weight, int velocity, int agility, int stamina,
                    int passing, int finishing, int marking, int dribbling, int technique, int longShots,
                    int impulsion, Position position, double price, double salary, int currentTeam) {

        super(id, name, height, weight, agility, passing, impulsion, technique, price, salary, currentTeam);
        this.velocity = velocity;
        this.finishing = finishing;
        this.marking = marking;
        this.dribbling = dribbling;
        this.longShots = longShots;
        this.position = position;
        this.stamina = stamina;

    }

    public int getFinishing() {return finishing;}
    public int getMarking() {return marking;}
    public int getDribbling() {return dribbling;}
    public int getLongShots() {return longShots;}
    public int getVelocity() {return velocity;}
    public int getStamina() {return stamina;}
    public Position getPosition() {return position;}
    public Position getCurrentPosition() {return currentPosition;}

    public void setCurrentPosition(Position currentPosition) {this.currentPosition = currentPosition;}
    public void setFinishing(int finishing) {this.finishing = finishing;}
    public void setMarking(int marking) {this.marking = marking;}
    public void setDribbling(int dribbling) {this.dribbling = dribbling;}
    public void setLongShots(int longShots) {this.longShots = longShots;}
    public void setVelocity(int velocity) {this.velocity = velocity;}
    public void setStamina(int stamina) {this.stamina = stamina;}

    @Override
    protected int jumpReach(){
        int minStamina = 12;
        return super.jumpReach() - staminaPenalty(minStamina);
    }

    @Override
    public void injuryRisk(){
        int minStamina = 12;
        double injuryChance = Math.random() * 20 + (double)getWeight() / 10 + staminaPenalty(minStamina);
        if (injuryChance > 20){
            setInjury(true, injuryGravity());
        }
    }

    private int speed(){
        int fullSpeed = Value.normalize(velocity + (int)Math.round(0.3 * (20-getWeight()) + 0.7 * getHeight()), 40);
        int minStamina = 14;
        return fullSpeed - staminaPenalty(minStamina);
    }

    private int strength(){
        int fullStrength = (int)Math.round(0.3 * getHeight() + 0.7 * getWeight());
        int minStamina = 14;
        return fullStrength - staminaPenalty(minStamina);
    }

    private int staminaPenalty(int penalty){
        return stamina < penalty ? (int)Math.round((double)(penalty - stamina) / 3) : 0;
    }

    @Override
    public double competence(){
        return switch (position) {
            case DEFENSE -> defensiveCompetence();
            case MIDFIELD -> midfieldCompetence();
            case ATTACK -> attackCompetence();
        };
    }

    public double defensiveCompetence(){
        return Value.normalize(marking + getTechnique() + jumpReach() + strength() + getAgility() + getPassing() + speed(), 140.0);
    }

    public double midfieldCompetence(){
        return Value.normalize(marking + dribbling + longShots + getTechnique() + jumpReach() + strength() + getAgility() + getPassing() + speed(), 180.0);
    }

    public double attackCompetence(){
        return Value.normalize( finishing + dribbling + longShots + getTechnique() + jumpReach() + strength() + getAgility() + getPassing() + speed(), 200.0);
    }

    @Override
    public double inGameCompetence(){
        double multiplier = 1;
        int minStamina = 14;

        switch (currentPosition) {
            case DEFENSE: {
                multiplier = position.getMultiplier()[0];
                break;
            }
            case MIDFIELD: {
                multiplier = position.getMultiplier()[1];
                break;
            }
            case ATTACK: {
                multiplier = position.getMultiplier()[2];
                break;
            }
        }
        return Math.random() * 3 + multiplier * competence() - staminaPenalty(minStamina);
    }



}
