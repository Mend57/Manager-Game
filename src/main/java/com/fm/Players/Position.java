package com.fm.Players;

public enum Position {
    DEFENSE(new double[]{1.0, 0.65, 0.2}),
    MIDFIELD(new double[]{0.75, 1, 0.75}),
    ATTACK(new double[]{0.2, 0.65, 1.0});

    private double[] multiplier;

    Position(double[] multiplier) {
        this.multiplier = multiplier;
    }
    public double[] getMultiplier() {
        return multiplier;
    }
}
