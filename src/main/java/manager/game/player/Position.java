package manager.game.player;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public enum Position {
    DEFENSE(Map.of("DEFENSE", 1.0, "MIDFIELD", 0.65, "ATTACK", 0.2)),
    MIDFIELD(Map.of("DEFENSE", 0.75, "MIDFIELD", 1.0, "ATTACK", 0.75)),
    ATTACK(Map.of("DEFENSE", 0.2, "MIDFIELD", 0.65, "ATTACK", 1.0));

    private final Map<String, Double> multiplier;

    Position(Map<String, Double> multiplier) {
        this.multiplier = Collections.unmodifiableMap(multiplier);
    }
}
