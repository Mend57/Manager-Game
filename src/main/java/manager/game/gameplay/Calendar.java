package manager.game.gameplay;

import lombok.Getter;
import lombok.Setter;
import manager.game.team.Team;

import java.time.LocalDate;

public class Calendar {

    @Getter @Setter
    private static int day = 15, month = 2, year = 2024;

    public static void addDays(int days) {
        LocalDate date     = LocalDate.of(year, month, day);
        LocalDate nextDate = date.plusDays(days);

        day   = nextDate.getDayOfMonth();
        month = nextDate.getMonthValue();
        year  = nextDate.getYear();

        refreshBirthdays();
    }

    public static void refreshBirthdays(){
        //for every player in the game
            //player.setAge()
    }
}
