import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class HippodromeTest {

    @Test
    void nullHorses() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Hippodrome(null)
        );
        assertEquals("Horses cannot be null.", exception.getMessage());
    }

    @Test
    void emptyHorses() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Hippodrome(new ArrayList<>())
        );
        assertEquals("Horses cannot be empty.", exception.getMessage());
    }

    @Test
    void getHorses() {
        List<Horse> horses = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            horses.add(new Horse("Horse" + i, i, i));
        }

        Hippodrome hippodrome = new Hippodrome(horses);
        List<Horse> returnedHorses = hippodrome.getHorses();

        assertEquals(horses, returnedHorses);
    }

    @Test
    void move() {
        List<Horse> horses = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            horses.add(mock(Horse.class));
        }

        Hippodrome hippodrome = new Hippodrome(horses);
        hippodrome.move();

        for (Horse horse : horses) {
            verify(horse).move();
        }
    }

    @Test
    void getWinner() {
        List<Horse> horses = new ArrayList<>();
        horses.add(new Horse("Horse1", 1, 10));
        horses.add(new Horse("Horse2", 1, 20));
        horses.add(new Horse("Horse3", 1, 15));

        Hippodrome hippodrome = new Hippodrome(horses);
        Horse winner = hippodrome.getWinner();

        assertEquals(20, winner.getDistance());
        assertEquals("Horse2", winner.getName());
    }
}
