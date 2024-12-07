package manager.game.team;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public enum Formation {
    FORMATION_4_4_2(Map.of("DEFENSE", 4, "MIDFIELD", 4, "ATTACK", 2)),
    FORMATION_4_3_3(Map.of("DEFENSE", 4, "MIDFIELD", 3, "ATTACK", 3)),
    FORMATION_3_5_2(Map.of("DEFENSE", 3, "MIDFIELD", 5, "ATTACK", 2));

    private final Map<String, Integer> formation;

    Formation(Map<String, Integer> formation) {
        this.formation = Collections.unmodifiableMap(formation);
    }

}
