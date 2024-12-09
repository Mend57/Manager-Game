package manager.game.gameplay;

import lombok.Getter;
import lombok.Setter;
import manager.game.team.Team;

import java.time.LocalDate;

public class CoreGameplay {

    @Getter @Setter
    private static int day = 15, month = 2, year = 2024;

    @Getter @Setter private static Team myTeam;

    public static void addDate(int days) {
        LocalDate date = LocalDate.of(year, month, day);
        LocalDate nextDate = date.plusDays(days);

        day = nextDate.getDayOfMonth();
        month = nextDate.getMonthValue();
        year = nextDate.getYear();
    }


    // Implement this
    public static void simulateMatch(){

    }


}
