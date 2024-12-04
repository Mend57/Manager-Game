package com.fm.Leagues;

public enum Tactic {
    T442(new int[]{4,4,2}),
    T352(new int[]{3,5,2}),
    T433(new int[]{4,3,3});

    int[] tactic;
    Tactic(int[] tactic) {
        this.tactic = tactic;
    }
}
