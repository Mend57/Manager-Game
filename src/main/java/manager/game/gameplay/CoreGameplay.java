package manager.game.gameplay;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CoreGameplay {

    private static int day = 15;
    private static int month = 3;
    private static int year = 2024;

    public void addDate(int days) {
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
